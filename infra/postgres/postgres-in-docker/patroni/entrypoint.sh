#!/bin/bash
set -e

# Initialize environment
export PATH="$VIRTUAL_ENV/bin:$PATH:/usr/lib/postgresql/15/bin"
export PATRONI_POSTGRESQL_DATA_DIR="/data/patroni"
export PATRONI_POSTGRESQL_PGPASS="/tmp/pgpass"

# Ensure data directory has correct permissions
if [ ! -d "$PATRONI_POSTGRESQL_DATA_DIR" ]; then
    mkdir -p "$PATRONI_POSTGRESQL_DATA_DIR"
    chmod 700 "$PATRONI_POSTGRESQL_DATA_DIR"
fi

# Start Patroni
exec patroni /etc/patroni.yml
