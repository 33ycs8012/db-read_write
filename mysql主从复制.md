[toc]

## 1.创建主从数据库

使用docker 创建数据库

​	创建主数据库：

```docker
docker run --name mysql1 -p 33061:3306 -e MYSQL_ROOT_PASSWORD=123 -d mysql:5.7 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
```

  创建从数据库：

```docker
docker run --name mysql2 -p 33062:3306 -e MYSQL_ROOT_PASSWORD=123 -d mysql:5.7 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
```

使用客户端工具测试链接。

使用以下命令查看运行的数据库：

```docker
docker ps
```

## 2.为主数据库分配用户

```mysql
grant replication slave on *.* 'rep1'@'%' identified by '123'
```

## 3.开启binlog

### 1.主数据库容器

```docker
docker exec -it mysql1 /bin/bash
cat /etc/mysql/mysql.conf.d/mysqld.cnf
```

复制mysqld.cnf文件内容。

退出容器：

```docker
exit
```

创建新的 mysqld.cnf 文件，并且将复制的内容占到新的 mysqld.cnf 中

```linux
vi mysqld.cnf
```

修改mysqld.cnf:

```linux
log-bin=/var/lib/mysql/binlog
server-id=1
binlog-do-db=javaboydb
```

:wq  保存退出

```docker
docker cp ./mysqld.cnf mysql1:/etc/mysql/mysql.conf.d/
docker restart mysql1
```

```mysql
在数据库中查看是否配置成功
show master status;
```

### 2.从数据库容器

```docker
docker exec -it mysql2 /bin/bash
cat /etc/mysql/mysql.conf.d/mysqld.cnf
```

复制mysqld.cnf文件内容。

退出容器：

```docker
exit
```

创建新的 mysqld.cnf 文件，并且将复制的内容占到新的 mysqld.cnf 中

```linux
vi mysqld.cnf
```

修改mysqld.cnf:

```linux
server-id=2
```

:wq  保存退出

```docker
docker cp ./mysqld.cnf mysql2:/etc/mysql/mysql.conf.d/
docker restart mysql2
```

```docker
docker exec -it mysql2 /bin/bash
mysql -u root -p
123
```

```docker
change master to master_host='123.57.109.177',master_port=33061,master_user='rep1',master_password='123',master_log_file='binlog.000001',master_log_pos=737;
```

```docker.mysql
start slave;
```

```docker.mysql
show slave status\G;
```



查看是否成功：

Slave_IO_Running:Yes

Slave_SQL_Running:Yes

## 4.最终测试

在数据库客户端中链接主数据库库，在主数据库中创建javaboydb数据库，创建user表，查看从数据库中是否有javaboydb数据库和user表