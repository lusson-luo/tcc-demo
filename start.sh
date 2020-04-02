# todo: 开启远程debug
cd eureka-server
gradle bootrun &
cd ../locker-server
gradle bootrun &
cd ../order-server
gradle bootrun &
cd ../storage-server
gradle bootrun &

