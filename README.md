# MWallet

**Mwallet** adalah sistem microservices dompet digital yang dirancang dengan arsitektur modern menggunakan Spring Boot, Kafka, Elasticsearch, dan Redis. Project ini mendukung fitur transaksi real-time, pencatatan history ke Elastic, dan sistem manajemen user.

## Prasyarat

Sebelum memulai, pastikan perangkat kamu sudah terinstall:

- Java 17 (Amazon Corretto atau OpenJDK)

- Maven 3.x

- Docker Desktop (dengan WSL2 backend untuk Windows atau Docker Engine untuk Linux)

- IntelliJ IDEA (Rekomendasi IDE)
- PostgreSQL local atau online service

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

Sebelumnya, set dahulu value ke .env, untuk formatnya bisa dilihat di .env.example

Jalankan command berikut untuk menjalankan docker compose
```
docker compose up -d --build
```
tunggu sampai semua startup selesai, 
<img width="1105" height="626" alt="Image" src="https://github.com/user-attachments/assets/bde7f81f-4764-4f19-b055-3ad8d5a31a40" />
kita bisa cek apakah service sudah berjalan dengan syntax
```
docker ps
```
<img width="1914" height="236" alt="Image" src="https://github.com/user-attachments/assets/39ed50d3-9b73-4ad5-b10e-53ea0d2cb631" />
Setelah semuanya berjalan, kamu bisa cek ketiga service tersebut (hanya eureka, gateway, dan elasticSearch yang di expose keluar)
- Eureka Server	http://localhost:9761	Service Discovery Dashboard
- API Gateway	http://localhost:9080	Entry point untuk semua API
- Elasticsearch	http://localhost:9200	Search Engine Node

<img width="1911" height="988" alt="Image" src="https://github.com/user-attachments/assets/4e5b730b-c055-4682-9a78-e99c03db1938" />
<img width="966" height="535" alt="Image" src="https://github.com/user-attachments/assets/a5e8f22f-29c9-4b21-a991-8724c328fde6" />

note : jika elastic gagal berjalan khususnya pada platform windows pastikan memory virtual WSL sudah diatur dengan syntax 
```
wsl -d docker-desktop -e sysctl -w vm.max_map_count=262144
```
## Documentation
[Postman Documentation](https://pastebin.com/Ltbq9MAJ)
