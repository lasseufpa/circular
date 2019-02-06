FROM mysql

ENV MYSQL_DATABASE db
ENV MYSQL_ROOT_PASSWORD root

COPY ./scripts/ /docker-entrypoint-initdb.d/
