set -e
set -u

function create_user_and_database() {
	local database=$(echo $1 | tr ',' ' ' | awk  '{print $1}')
	local owner=$(echo $1 | tr ',' ' ' | awk  '{print $2}')
	local password=$(echo $1 | tr ',' ' ' | awk  '{print $3}')
	local script=$(echo $1 | tr ',' ' ' | awk  '{print $4}')
	echo "Creating user and database '$database' with user '$owner' and password '$password'"
	psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
	    CREATE DATABASE "$database";
	    CREATE USER "$owner" WITH ENCRYPTED PASSWORD '$password';
	    GRANT ALL PRIVILEGES ON DATABASE "$database" TO "$owner";
EOSQL
  if [ -n "$script" ]; then
    echo "Running script $script for database $database"
    psql -U "$owner" -d "$database" -a -f /home/"$script"
  fi
}

if [ -n "$POSTGRES_MULTIPLE_DATABASES" ]; then
	echo "Multiple database creation requested: $POSTGRES_MULTIPLE_DATABASES"
	for db in $(echo $POSTGRES_MULTIPLE_DATABASES | tr ';' ' '); do
		create_user_and_database $db
	done
	echo "Multiple databases created"
fi