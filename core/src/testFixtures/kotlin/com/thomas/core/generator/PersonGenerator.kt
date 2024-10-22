package com.thomas.core.generator

import com.thomas.core.data.PersonTestData
import com.thomas.core.model.general.Gender
import java.time.LocalDate
import java.util.UUID

@Suppress("MagicNumber")
object PersonGenerator {

    private val minBirthDate = LocalDate.of(1970, 1, 1).toEpochDay()
    private val maxBirthDate = LocalDate.of(2000, 12, 31).toEpochDay()

    private val firstNames = listOf(
        "Adan",
        "Anaya",
        "Terrell",
        "Kelton",
        "Kamden",
        "Hassan",
        "Teagan",
        "Elias",
        "Dexter",
        "Fisher",
        "Clarence",
        "Yazmin",
        "Siena",
        "Saige",
        "Jennifer",
        "Lindsey",
        "Taylor",
        "Jan",
        "Andres",
        "Cesar",
        "Kamren",
        "Javion",
        "Ronin",
        "Barbara",
        "Rex",
        "Abigayle",
        "Winston",
        "Joseph",
        "Claire",
        "Cara",
        "Leila",
        "Konner",
        "Charles",
        "Athena",
        "Miranda",
        "Cameron",
        "Kinsley",
        "Maleah",
        "Parker",
        "Mackenzie",
        "Branden",
        "Hope",
        "Kendal",
        "Matteo",
        "Kara",
        "Chelsea",
        "Quincy",
        "Emery",
        "Briana",
        "Nelson",
        "Reina",
        "Hector",
        "Dennis",
        "Kenley",
        "Ethan",
        "Yael",
        "Eve",
        "Talan",
        "Anthony",
        "Ronin",
        "Erica",
        "Elisa",
        "Marissa",
        "Sanai",
        "India",
        "Kaleigh",
        "Kathryn",
        "Jaylin",
        "Amber",
        "Jaylah",
        "Finn",
        "Trevor",
        "Martha",
        "Jovanny",
        "Nikolai",
        "Tanya",
        "Malakai",
        "Jaidyn",
        "London",
        "Carson",
        "Luciana",
        "Jett",
        "Kareem",
        "Leonard",
        "Maximo",
        "Charlie",
        "Mariela",
        "Nancy",
        "Atticus",
        "Serena",
        "Keon",
        "Kameron",
        "Brooklyn",
        "Adelyn",
        "Desiree",
        "April",
        "Jamarion",
        "Alisson",
        "Jocelynn",
        "Maeve",
        "Ana",
        "João",
        "Maria",
        "Antônio",
        "Júlio",
        "Cláudio",
        "André",
        "Márcio",
        "Luís",
        "Sérgio",
        "Francisco",
        "Fernanda",
        "Átila",
        "Eliane",
        "Álvaro",
        "Patrícia",
        "Caio",
        "Mário",
        "Lívia",
        "Hélio",
        "Bárbara",
        "César",
        "Débora",
        "Lúcio",
        "Ágata",
        "Fábio",
        "Júlia",
        "Márcia",
        "Lázaro",
        "Sérgio",
        "Ângela",
        "Jéssica",
        "Túlio",
        "Márcio",
        "Márcia",
        "Hélio",
        "Eliana",
        "Júlio",
        "Ângelo",
        "Mário",
        "Célia",
        "Fábio",
        "Áurea",
        "João",
        "Lígia",
        "Flávio",
        "Álvaro",
        "Andréa",
        "Caio",
        "Lígia",
        "César",
        "Márcio",
        "Ângela",
        "Lúcio",
        "Júlio",
        "Álvaro",
        "Patrícia",
        "Fábio",
        "Júlia",
        "Lázaro",
        "Sérgio",
        "Ângelo",
        "Jéssica",
        "Túlio",
        "Márcia",
        "Hélio",
        "Eliana",
        "Júlio",
        "Ângela",
        "Mário",
        "Célia",
        "Flávio",
        "Áurea",
        "João",
        "Lívia",
        "Márcio",
        "Lígia",
        "César",
        "Márcio",
        "Ângela",
        "Lúcio",
        "Júlio",
        "Álvaro",
        "Patrícia",
        "Fábio",
        "Júlia",
        "Lázaro",
        "Sérgio",
        "Ângelo",
        "Jéssica",
        "Túlio",
        "Márcia",
        "Hélio",
        "Eliana",
        "Júlio",
        "Ângela",
        "Mário",
        "Célia",
        "Flávio",
        "Áurea",
    )

    private val lastNames = listOf(
        "Fleming",
        "Rowe",
        "Simmons",
        "Morrison",
        "Olsen",
        "Mendez",
        "Willis",
        "Arnold",
        "Holland",
        "Castro",
        "Alvarado",
        "Torres",
        "Reid",
        "Short",
        "Mays",
        "Nunez",
        "Washington",
        "Hebert",
        "Adams",
        "Mosley",
        "Rosales",
        "Hart",
        "Wells",
        "Conway",
        "Meza",
        "Jordan",
        "Buchanan",
        "Pham",
        "Mendez",
        "Herring",
        "Hines",
        "Shah",
        "Peterson",
        "Castaneda",
        "Russo",
        "Shields",
        "Spence",
        "Morris",
        "Montoya",
        "Mayer",
        "Mcbride",
        "Lozano",
        "Skinner",
        "Abbott",
        "Walter",
        "Wong",
        "Medina",
        "House",
        "Mclean",
        "Liu",
        "Dixon",
        "Walls",
        "Cole",
        "Humphrey",
        "Curry",
        "Sawyer",
        "Mccarthy",
        "Pollard",
        "Russo",
        "Mclean",
        "Madden",
        "Jenkins",
        "Johnson",
        "Thompson",
        "Cabrera",
        "Hamilton",
        "Harding",
        "Buck",
        "Mcbride",
        "Zavala",
        "Sullivan",
        "Sullivan",
        "Espinoza",
        "Fletcher",
        "Schmidt",
        "Ellis",
        "Skinner",
        "York",
        "Wise",
        "Dixon",
        "Cuevas",
        "Molina",
        "Cuevas",
        "Snow",
        "Watson",
        "Farrell",
        "Orozco",
        "Velez",
        "Blackburn",
        "Torres",
        "Li",
        "Santana",
        "Bright",
        "Hines",
        "Robles",
        "Nolan",
        "Malone",
        "Gould",
        "Huffman",
        "House",
        "Júlia",
        "Pedro",
        "Clara",
        "Carlos",
        "César",
        "Henrique",
        "Luiz",
        "Augusto",
        "Felipe",
        "Ricardo",
        "José",
        "Lúcia",
        "Henrique",
        "Márcia",
        "Gabriel",
        "Helena",
        "Antônio",
        "Sérgio",
        "Cristina",
        "Roberto",
        "Regina",
        "Augusto",
        "Raquel",
        "Flávio",
        "Luísa",
        "Henrique",
        "Beatriz",
        "Helena",
        "Mateus",
        "Luiz",
        "Maria",
        "Fernanda",
        "César",
        "Paulo",
        "Patrícia",
        "Antônio",
        "Patrícia",
        "Alberto",
        "Rafael",
        "Jorge",
        "Regina",
        "Luiz",
        "Cristina",
        "Vítor",
        "Maria",
        "Henrique",
        "Augusto",
        "Regina",
        "Henrique",
        "Fernanda",
        "Gabriel",
        "Roberto",
        "Regina",
        "André",
        "Augusto",
        "Sérgio",
        "Márcia",
        "Mateus",
        "Regina",
        "Paulo",
        "Augusto",
        "Márcio",
        "Maria",
        "André",
        "Regina",
        "Flávio",
        "Lígia",
        "Rafael",
        "Débora",
        "Luís",
        "Fernanda",
        "Roberto",
        "Helena",
        "Augusto",
        "Andréa",
        "Mateus",
        "Helena",
        "Flávio",
        "Sérgio",
        "Lúcia",
        "Márcio",
        "Flávio",
        "André",
        "Júlia",
        "Paulo",
        "Fernanda",
        "Luís",
        "Fábio",
        "Mateus",
        "Júlia",
        "Flávio",
        "Ângela",
        "Caio",
        "Lúcia",
        "Márcio",
        "Andréa",
        "Túlio",
        "Patrícia",
        "Augusto",
        "Fernanda",
    )

    fun generatePerson() = PersonTestData(
        id = UUID.randomUUID(),
        firstName = firstNames.random(),
        lastName = lastNames.random(),
        documentNumber = generateDocumentNumber(),
        phoneNumber = (1..11).map { (0..9).random() }.joinToString(""),
        birthDate = generateBirthDate(),
        userGender = Gender.entries.random(),
    )

    private fun generateDocumentNumber(): String {
        val numbers = (1..9).map { (0..9).random() }
        val firstDigit = numbers.digits()
        val secondDigit = (numbers + firstDigit).digits()

        return "${numbers.joinToString("")}$firstDigit$secondDigit"
    }

    private fun List<Int>.digits(): Int {
        val firstNineDigits = this
        val weights = ((this.size + 1) downTo 2).toList()
        val sum = firstNineDigits.withIndex().sumOf { (index, element) -> weights[index] * element }

        val remainder = sum % 11
        return if (remainder < 2) 0 else 11 - remainder
    }

    private fun generateBirthDate() = (minBirthDate..maxBirthDate).random().let { LocalDate.ofEpochDay(it) }

}