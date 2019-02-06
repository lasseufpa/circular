run:
	docker run -d -p 3310:3306 --name circulardb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=circular mysql

exec:
	docker exec -it circulardb sh -c "exec mysql -h 127.0.0.1 -P 3306 -uroot -p"

remove:
	docker rm --force circulardb
