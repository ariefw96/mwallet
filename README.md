# MWallet

**Mwallet** adalah sistem microservices dompet digital yang dirancang dengan arsitektur modern menggunakan Spring Boot, Kafka, Elasticsearch, dan Redis. Project ini mendukung fitur transaksi real-time, pencatatan history ke Elastic, dan sistem manajemen user.

## Prasyarat

Sebelum memulai, pastikan perangkat kamu sudah terinstall:

- Java 17 (Amazon Corretto atau OpenJDK)

- Maven 3.x

- Docker Desktop (dengan WSL2 backend untuk Windows atau Docker Engine untuk Linux)

- IntelliJ IDEA (Rekomendasi IDE)

## Instalasi

1. Clone project terlebih dahulu
```
git clone https://github.com/ariefw96/mwallet.git
cd mwallet
```
2. Persiapan Project di IDE
- Buka folder project di IntelliJ IDEA.
- Tunggu IntelliJ melakukan resolve dependencies.
- Pastikan Project SDK diatur ke Java 17.

3. Build Library dan Parent

   Project ini menggunakan shared common-library jadi kita harus compile dan install dulu common-library nya
```
cd mwallet-repo
mvn clean compile install
cd ..
```
Lalu kita build parent nya dan tunggu sampai selesai
```
mvn clean install -DskipTests=true
```

## Deployment
Project ini sudah dilengkapi redis, kafka, dan elasticsearch yang sudah ada pada docker-compose.yml
notes : untuk database (PostgreSQL) sengaja dibuat terpisah 
Jalankan command berikut untuk menjalankan docker compose
```
docker compose up -d --build
```
tunggu sampai semua startup selesai, kita bisa cek apakah service sudah berjalan dengan syntax
```
docker ps
```
Setelah semuanya berjalan, kamu bisa cek ketiga service tersebut (hanya eureka, gateway, dan elasticSearch yang di expose keluar)
- Eureka Server	http://localhost:9761	Service Discovery Dashboard
- API Gateway	http://localhost:9080	Entry point untuk semua API
- Elasticsearch	http://localhost:9200	Search Engine Node

note : jika elastic gagal berjalan khususnya pada platform windows pastikan memory virtual WSL sudah diatur dengan syntax 
```
wsl -d docker-desktop -e sysctl -w vm.max_map_count=262144
```
## Documentation
[Postman Documentation](https://pastebin.com/Ltbq9MAJ)
