# IpFilterRuleProject

Bu README dosyası, IpFilterRuleProject'in çalıştırılması ve kullanılması hakkında temel bilgileri içermektedir.

## Projeyi Çalıştırma

Projeyi çalıştırabilmek için öncelikle `docker-compose.yml` dosyasını çalıştırmanız gerekmektedir. Bu işlem, proje
içerisinde kullanılan veritabanı entegrasyonunun başarılı bir şekilde oluşturulmasını sağlayacaktır.

```
docker-compose up
```

## API Dokümantasyonu

Proje içine eklenmiş olan OpenAPI entegrasyonu sayesinde, uygulama içerisindeki endpoint'lerin API dokümantasyonuna
aşağıdaki linkten erişebilirsiniz:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Veritabanı Operasyonları

Projede veritabanı operasyonları Liquibase kullanılarak gerçekleştirilmiştir. Bu, veritabanı şemasının version
kontrolünü ve yönetimini kolaylaştırmaktadır.

## Kural Yönetimi

Projede kural yöneticisi (rule manager) olarak Drools kullanılmıştır. Eklenen kurallar bu sistem aracılığıyla
işlenmektedir.
