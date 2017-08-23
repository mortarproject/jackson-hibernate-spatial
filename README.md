## Jackson Serializer/Deserializer module for Hibernate Spatial objects

Simply add a new instance of the module to your ObjectMapper:


```java
myObjectMapper.registerModule(new GeometryModule());
```