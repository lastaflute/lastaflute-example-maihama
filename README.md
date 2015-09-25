LastaFlute Example Maihama
=======================
example project for LastaFlute as multi-project (with MySQL)

LastaFlute:  
https://github.com/lastaflute/lastaflute

# Quick Trial
Can boot it by example of LastaFlute:

1. git clone https://github.com/lastaflute/lastaflute-example-maihama.git
2. prepare MySQL on 3306 port as empty root password or using *system-password.txt  
and execute by *ReplaceSchema at DBFlute client directory 'dbflute_maihamadb'  
3. compile it by Java8, on e.g. Eclipse or IntelliJ or ... as Maven project
4. execute the *main() method of (org.docksidestage.boot) DocksideBoot
5. access to http://localhost:8091/dockside  
and login by user 'Pixy' and password 'sea', and can see debug log at console.

*system-password.txt
```java
maihama-common
 |-dbflute_maihamadb
 | |-dfprop
 | |  |-...
 | |  |-system-password.txt // make file and write root password if not empty password
 | |-...
 |
 |-mydbflute
 |-...
```

*ReplaceSchema
```java
// call manage.sh at maihama-dockside/dbflute_maihamadb
// and select replace-schema in displayed menu
...:dbflute_maihamadb ...$ sh manage.sh
```

*main() method
```java
public class DocksideBoot {

    public static void main(String[] args) {
        new JettyBoot(8091, "/dockside").asDevelopment().bootAwait();
    }
}
```

# Information
## License
Apache License 2.0

## Official site
comming soon...
