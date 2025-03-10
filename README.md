# Ứng dụng Quản lý Bán Sách Trực Tuyến

## 1. Giới thiệu
Ứng dụng quản lý bán sách trực tuyến được xây dựng trên nền tảng Web, sử dụng Spring Boot cho backend và ReactJS cho frontend. Dự án giúp người dùng có thể mua sách trực tuyến, quản lý đơn hàng, quản lý thông tin cá nhân, và hỗ trợ admin trong việc quản lý nội dung.

## 2. Công nghệ sử dụng
### Backend:
- **Ngôn ngữ**: Java (JDK 11 trở lên)
- **Framework**: Spring Boot
- **Quản lý phụ thuộc**: Maven
- **Cơ sở dữ liệu**: MySQL / PostgreSQL (hoặc hệ quản trị cơ sở dữ liệu khác)

### Frontend:
- **Ngôn ngữ**: JavaScript / TypeScript
- **Thư viện**: ReactJS
- **Quản lý package**: Yarn / NPM

## 3. Cài đặt và Chạy ứng dụng
### Yêu cầu hệ thống
- Cài đặt **Java Development Kit (JDK) 11 trở lên**
- Cài đặt **Maven**
- Cài đặt **Node.js phiên bản 18.20.3**
- Cài đặt **Yarn**
- Cài đặt **Cơ sở dữ liệu MySQL / PostgreSQL** và cấu hình kết nối

### 3.1. Cài đặt Backend (Spring Boot)
#### Bước 1: Cài đặt các phụ thuộc bằng Maven
```sh
mvn install
```
#### Bước 2: Chạy ứng dụng Spring Boot
```sh
mvn spring-boot:run
```
#### Bước 3: Hoặc chạy từ file JAR sau khi build
```sh
mvn package
java -jar target/your-app.jar
```

### 3.2. Cài đặt Frontend (ReactJS)
#### Bước 1: Cài đặt các phụ thuộc
```sh
yarn install
```
#### Bước 2: Chạy server phát triển
```sh
yarn dev
```
Ứng dụng sẽ chạy tại `http://localhost:3000`.

#### Bước 3: Build ứng dụng
```sh
yarn build
```
Output build sẽ nằm trong thư mục `dist`.

#### Bước 4: Kiểm tra và định dạng code
- Kiểm tra codebase:
```sh
yarn lint
```
- Định dạng lại codebase:
```sh
yarn lint:fix
```

## 4. Cấu hình cơ sở dữ liệu
Chỉnh sửa file `application.properties` hoặc `application.yml` trong backend để cấu hình kết nối cơ sở dữ liệu. Ví dụ với MySQL:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bookstore_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

## 5. API Endpoints (Ví dụ)
- `POST /api/auth/login` - Đăng nhập
- `POST /api/auth/register` - Đăng ký
- `GET /api/books` - Lấy danh sách sách
- `POST /api/orders` - Đặt hàng

## 6. Thông tin thêm
Nếu có bất kỳ vấn đề nào trong quá trình cài đặt, hãy kiểm tra lại các bước hoặc liên hệ với nhóm phát triển để được hỗ trợ.
