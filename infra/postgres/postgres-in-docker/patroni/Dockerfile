FROM postgres:15

ENV DEBIAN_FRONTEND=noninteractive

# Install required packages
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    python3-pip \
    python3-dev \
    python3-venv \
    python3-full \
    libpq-dev \
    curl \
    jq \
    gcc \
    python3-setuptools \
    && rm -rf /var/lib/apt/lists/*

# Create and activate virtual environment
ENV VIRTUAL_ENV=/opt/venv
RUN python3 -m venv $VIRTUAL_ENV
ENV PATH="$VIRTUAL_ENV/bin:$PATH"

# Install Python packages in virtual environment
RUN pip3 install --no-cache-dir --upgrade pip && \
    pip3 install --no-cache-dir patroni[etcd] psycopg2-binary

# Create directory for PostgreSQL data with correct permissions
RUN mkdir -p /data && \
    chown postgres:postgres /data && \
    mkdir -p /data/patroni && \
    chown -R postgres:postgres /data/patroni && \
    chmod -R 700 /data/patroni

# Copy Patroni configuration and entrypoint script
COPY patroni.yml /etc/patroni.yml
COPY entrypoint.sh /

RUN chown postgres:postgres /etc/patroni.yml && \
    chown postgres:postgres /entrypoint.sh && \
    chmod +x /entrypoint.sh

USER postgres

ENTRYPOINT ["/entrypoint.sh"]
