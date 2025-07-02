package com.thomas.locality.domain

import com.thomas.cache.handler.CacheHandler
import com.thomas.cache.handler.SimpleCacheHandler
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.data.securityUser
import com.thomas.core.data.securityUserMaster
import com.thomas.core.model.general.AddressState
import com.thomas.core.util.StringUtils.randomString
import com.thomas.locality.data.entity.AddressEntity
import com.thomas.locality.data.repository.AddressRepository
import com.thomas.locality.domain.adapter.AddressServiceAdapter
import com.thomas.locality.domain.model.extension.toAddressResponse
import com.thomas.locality.port.address.AddressNotFoundException
import com.thomas.locality.port.address.AddressSearchPort
import com.thomas.locality.port.address.model.response.AddressSearchResponse
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test


class AddressServiceTest {

    private val masterUser = securityUserMaster
    private val commonUser = securityUser

    private val searchResponses: MutableMap<String, AddressSearchResponse> = mutableMapOf()
    private val repositoryData: MutableMap<String, AddressEntity> = mutableMapOf()

    private val addressPort: AddressSearchPort = object : AddressSearchPort {
        override suspend fun searchByZipcode(zipcode: String): AddressSearchResponse {
            return searchResponses[zipcode] ?: throw AddressNotFoundException(zipcode)
        }
    }

    private val cacheHandler: CacheHandler = SimpleCacheHandler()

    private val addressRepository: AddressRepository = object : AddressRepository {
        override suspend fun create(
            entity: AddressEntity
        ): AddressEntity = entity.apply { repositoryData[this.zipcodeNumber] = this }

        override suspend fun update(
            entity: AddressEntity
        ): AddressEntity = entity.apply { repositoryData[this.zipcodeNumber] = this }

        override suspend fun findByZipcode(
            zipcode: String
        ): AddressEntity? = repositoryData[zipcode]

    }

    private val addressService: AddressService = AddressServiceAdapter(
        addressPort = addressPort,
        cacheHandler = cacheHandler,
        addressRepository = addressRepository
    )

    private val addressEntity: AddressEntity
        get() = AddressEntity(
            zipcodeNumber = (10_000_000..99_999_999).random().toString(),
            addressStreet = randomNullableName(),
            addressComplement = randomNullableName(),
            addressUnit = randomNullableName(),
            addressNeighborhood = randomNullableName(),
            addressCity = randomName(),
            addressState = AddressState.entries.random(),
            cityCode = randomNullableName(),
            referenceCode = randomNullableName(),
            phoneCode = (10..99).random().toString(),
            federalCode = randomNullableName(),
        )

    private val addressSearchResponse: AddressSearchResponse
        get() = addressEntity.toAddressSearchResponse()

    private fun AddressEntity.toAddressSearchResponse() = AddressSearchResponse(
        zipcodeNumber = this.zipcodeNumber,
        addressStreet = this.addressStreet,
        addressComplement = this.addressComplement,
        addressUnit = this.addressUnit,
        addressNeighborhood = this.addressNeighborhood,
        addressCity = this.addressCity,
        addressState = this.addressState,
        cityCode = this.cityCode,
        referenceCode = this.referenceCode,
        phoneCode = this.phoneCode,
        federalCode = this.federalCode,
    )

    private fun randomName(): String =
        randomString(length = 20, numbers = false, spaces = true)

    private fun randomNullableName(): String? = randomName().takeIf {
        (0..1).random() == 0
    }

    @Test
    fun `Force update TRUE and user is MASTER then update Address`() = runTest {
        currentUser = masterUser

        val entity = addressEntity.apply { repositoryData[this.zipcodeNumber] = this }
        addressSearchResponse.copy(zipcodeNumber = entity.zipcodeNumber).apply { searchResponses[this.zipcodeNumber] = this }

        val result = addressService.addressByZipcode(entity.zipcodeNumber, forceUpdate = true)

        assert(result == repositoryData[entity.zipcodeNumber]!!.toAddressResponse())
    }

    @Test
    fun `Force update TRUE and user is MASTER then create Address`() = runTest {
        currentUser = masterUser

        val searchResponse = addressSearchResponse.apply { searchResponses[this.zipcodeNumber] = this }

        val result = addressService.addressByZipcode(searchResponse.zipcodeNumber, forceUpdate = true)

        assert(result == repositoryData[searchResponse.zipcodeNumber]!!.toAddressResponse())
    }

    @Test
    fun `Force update TRUE and user is NOT MASTER then get from cache`() = runTest {
        currentUser = commonUser

        val entity = addressEntity
        cacheHandler.set(entity.zipcodeNumber, entity)

        val result = addressService.addressByZipcode(entity.zipcodeNumber, forceUpdate = true)

        assert(result == entity.toAddressResponse())
    }

    @Test
    fun `Force update TRUE and user is NOT MASTER then get from repository`() = runTest {
        currentUser = commonUser

        val entity = addressEntity.apply { repositoryData[this.zipcodeNumber] = this }

        val result = addressService.addressByZipcode(entity.zipcodeNumber, forceUpdate = true)

        assert(result == entity.toAddressResponse())
    }

    @Test
    fun `Force update TRUE and user is NOT MASTER then search and create`() = runTest {
        currentUser = commonUser

        val searchResponse = addressSearchResponse.apply { searchResponses[this.zipcodeNumber] = this }

        val result = addressService.addressByZipcode(searchResponse.zipcodeNumber, forceUpdate = true)

        assert(result == repositoryData[searchResponse.zipcodeNumber]!!.toAddressResponse())
    }

    @Test
    fun `Force update FALSE then get from cache`() = runTest {
        currentUser = commonUser

        val entity = addressEntity
        cacheHandler.set(entity.zipcodeNumber, entity)

        val result = addressService.addressByZipcode(entity.zipcodeNumber, forceUpdate = false)

        assert(result == entity.toAddressResponse())
    }

    @Test
    fun `Force update FALSE then get from repository`() = runTest {
        currentUser = commonUser

        val entity = addressEntity.apply { repositoryData[this.zipcodeNumber] = this }

        val result = addressService.addressByZipcode(entity.zipcodeNumber, forceUpdate = false)

        assert(result == entity.toAddressResponse())
    }

    @Test
    fun `Force update FALSE then search and create`() = runTest {
        currentUser = commonUser

        val searchResponse = addressSearchResponse.apply { searchResponses[this.zipcodeNumber] = this }

        val result = addressService.addressByZipcode(searchResponse.zipcodeNumber, forceUpdate = false)

        assert(result == repositoryData[searchResponse.zipcodeNumber]!!.toAddressResponse())
    }

    @Test
    fun `Cache priority over repository when both exist`() = runTest {
        currentUser = commonUser

        val entityInRepository = addressEntity.apply { repositoryData[this.zipcodeNumber] = this }
        val entityInCache = addressEntity.copy(
            zipcodeNumber = entityInRepository.zipcodeNumber,
            addressCity = "Cached City"
        )
        cacheHandler.set(entityInCache.zipcodeNumber, entityInCache)

        val result = addressService.addressByZipcode(entityInCache.zipcodeNumber, forceUpdate = false)

        assert(result == entityInCache.toAddressResponse())
    }

    @Test
    fun `Address is cached after retrieval from repository`() = runTest {
        currentUser = commonUser

        val entity = addressEntity.apply { repositoryData[this.zipcodeNumber] = this }

        val result = addressService.addressByZipcode(entity.zipcodeNumber, forceUpdate = false)

        val cachedEntity = cacheHandler.get<AddressEntity>(entity.zipcodeNumber)
        assert(cachedEntity == entity)
        assert(result == entity.toAddressResponse())
    }

    @Test
    fun `Address is cached after creation from search`() = runTest {
        currentUser = commonUser

        val searchResponse = addressSearchResponse.apply { searchResponses[this.zipcodeNumber] = this }

        val result = addressService.addressByZipcode(searchResponse.zipcodeNumber, forceUpdate = false)

        val cachedEntity = cacheHandler.get<AddressEntity>(searchResponse.zipcodeNumber)
        assert(cachedEntity == repositoryData[searchResponse.zipcodeNumber])
        assert(result == repositoryData[searchResponse.zipcodeNumber]!!.toAddressResponse())
    }

    @Test
    fun `Address is cached after update when master user forces update`() = runTest {
        currentUser = masterUser

        val entity = addressEntity.apply { repositoryData[this.zipcodeNumber] = this }
        val searchResponse = addressSearchResponse.copy(
            zipcodeNumber = entity.zipcodeNumber,
            addressCity = "Updated City"
        ).apply { searchResponses[this.zipcodeNumber] = this }

        val result = addressService.addressByZipcode(entity.zipcodeNumber, forceUpdate = true)

        val cachedEntity = cacheHandler.get<AddressEntity>(entity.zipcodeNumber)
        assert(cachedEntity == repositoryData[entity.zipcodeNumber])
        assert(result == repositoryData[entity.zipcodeNumber]!!.toAddressResponse())
    }

    @Test
    fun `Exception thrown when address not found anywhere`() = runTest {
        currentUser = commonUser

        val nonExistentZipcode = "99999999"

        try {
            addressService.addressByZipcode(nonExistentZipcode, forceUpdate = false)
            assert(false) { "Expected AddressNotFoundException to be thrown" }
        } catch (e: AddressNotFoundException) {
            assert(e.message?.contains(nonExistentZipcode) == true)
        }
    }

    @Test
    fun `Exception thrown when master user forces update but address not found in search`() = runTest {
        currentUser = masterUser

        val entity = addressEntity.apply { repositoryData[this.zipcodeNumber] = this }
        val nonExistentZipcode = "99999999"

        try {
            addressService.addressByZipcode(nonExistentZipcode, forceUpdate = true)
            assert(false) { "Expected AddressNotFoundException to be thrown" }
        } catch (e: AddressNotFoundException) {
            assert(e.message?.contains(nonExistentZipcode) == true)
        }
    }

    @Test
    fun `Force update TRUE and user is MASTER but address not found in repository then create new`() = runTest {
        currentUser = masterUser

        val searchResponse = addressSearchResponse.apply { searchResponses[this.zipcodeNumber] = this }
        
        val result = addressService.addressByZipcode(searchResponse.zipcodeNumber, forceUpdate = true)

        assert(repositoryData.containsKey(searchResponse.zipcodeNumber))
        assert(result == repositoryData[searchResponse.zipcodeNumber]!!.toAddressResponse())
    }

    @Test
    fun `Force update FALSE with null cache and null repository then search and create`() = runTest {
        currentUser = commonUser

        val searchResponse = addressSearchResponse.apply { searchResponses[this.zipcodeNumber] = this }
                
        val result = addressService.addressByZipcode(searchResponse.zipcodeNumber, forceUpdate = false)

        assert(repositoryData.containsKey(searchResponse.zipcodeNumber))
        assert(result == repositoryData[searchResponse.zipcodeNumber]!!.toAddressResponse())
    }

    @Test
    fun `Address steps with null cache but found in repository`() = runTest {
        currentUser = commonUser

        val entity = addressEntity.apply { repositoryData[this.zipcodeNumber] = this }
        
        val result = addressService.addressByZipcode(entity.zipcodeNumber, forceUpdate = false)

        assert(result == entity.toAddressResponse())
    }

}