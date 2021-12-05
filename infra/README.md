# Infra

## Start infra
```sh

# Start infra
docker-compose up -d

# Connect to postgresql
docker-compose run photonidb bash
psql --host=photonidb --username=user --dbname=photonidb

## Connect to db
\c photonidb
```