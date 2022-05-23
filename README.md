# Common library

## Installation

To connect to the project in gradle.kts connect the maven repository.

Dependent on event lib

```kotlin
repositories {
    maven {
        url = uri("https://gitlab.com/api/v4/projects/36319712/packages/maven")
        authentication {
            create<HttpHeaderAuthentication>("header")
        }
        credentials(HttpHeaderCredentials::class) {
            name = "Deploy-Token"
            value = System.getenv("CI_DEPLOY_PASSWORD")
        }
    }
}

dependencies {
    implementation("com.briolink.lib:common")
}

```

## Documentation

### Exceptions

To create your own exception, you must inherit from
the [IBlException](/src/main/kotlin/com/briolink/lib/common/exception/base/IBlException.kt) interface or inherit from an
existing [base class](/src/main/kotlin/com/briolink/lib/common/exception/base).

```kotlin
interface IBlException {
    /**
     * Code i18n in resource messages
     */
    val code: String

    /**
     * List arguments in i18n code
     */
    val arguments: Array<String>?

    /**
     * Http status
     */
    val httpsStatus: HttpStatus
}
```

Arguments this is optional, if you don't need arguments, you can use null, but if messages specified arguments:

```
exception.service.unavailable=Service unavailable: {0}
```

then you must add them to the arguments list.

#### The classes provided:

| Base class                                                                                                        | Class                                                                                                    | Message                                                                         | Http code                 | 
|-------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------|---------------------------|
| [BaseAccessDeniedException](/src/main/kotlin/com/briolink/lib/common/exception/base/BaseAccessDeniedException.kt) | [AccessDeniedException](/src/main/kotlin/com/briolink/lib/common/exception/AccessDeniedException.kt)     | ``` exception.access-denied=Access denied ```                                   | 403 (FORBIDDEN)           |
| [BaseBadRequestException](/src/main/kotlin/com/briolink/lib/common/exception/base/BaseBadRequestException.kt)     | [BadRequestException](/src/main/kotlin/com/briolink/lib/common/exception/BadRequestException.kt)         | ```exception.bad-request=Bad request```                                         | 400 (BAD_REQUEST)         |
| [BaseExistException](/src/main/kotlin/com/briolink/lib/common/exception/base/BaseExistException.kt)               | [EntityExistException](/src/main/kotlin/com/briolink/lib/common/exception/EntityExistException.kt)       | ```exception.entity.exist=Entity exist```                                       | 409 (CONFLICT)            |
| [BaseFileTypeException](/src/main/kotlin/com/briolink/lib/common/exception/base/BaseFileTypeException.kt)         | [FileTypeException](/src/main/kotlin/com/briolink/lib/common/exception/FileTypeException.kt)             | ```exception.file.type.invalid=Invalid file type: allowed JPEG or PNG images``` | 400 (BAD_REQUEST)         |
| [BaseNotFoundException](/src/main/kotlin/com/briolink/lib/common/exception/base/BaseNotFoundException.kt)         | [EntityNotFoundException](/src/main/kotlin/com/briolink/lib/common/exception/EntityNotFoundException.kt) | ```exception.entity.not-found=Entity not found```                               | 404 (NOT_FOUND)           |
| [BaseUnavailableException](/src/main/kotlin/com/briolink/lib/common/exception/base/BaseUnavailableException.kt)   | [UnavailableException](/src/main/kotlin/com/briolink/lib/common/exception/UnavailableException.kt)       | ```exception.service.unavailable=Service unavailable: {0}```                    | 503 (SERVICE_UNAVAILABLE) |
| [BaseValidationException](/src/main/kotlin/com/briolink/lib/common/exception/base/BaseUnavailableException.kt)    | [ValidationException](/src/main/kotlin/com/briolink/lib/common/exception/ValidationException.kt)         | ```exception.validation.error=Validation error```                               | 400 (BAD_REQUEST)         |

### Validation

This table provides [annotations](/src/main/kotlin/com/briolink/lib/common/validation) that validate the class and
causes a ConstraintViolationException if the validation is broken.

| Annotation class                                                                                 | Description                                          | Default message                                                                                    |
|--------------------------------------------------------------------------------------------------|------------------------------------------------------|----------------------------------------------------------------------------------------------------|
| [NullOrNotBlank](src/main/kotlin/com/briolink/lib/common/validation/NullOrNotBlank.kt)           | String must be null or not blank                     | ```validation.null-or-not-blank=Value must be null or not blank```                                 |
| [NullOrPastOrPresent](src/main/kotlin/com/briolink/lib/common/validation/NullOrPastOrPresent.kt) | java.time must be null past or preset                | ```{javax.validation.constraints.PastOrPresent.message}```                                         |
| [NullOrURL](src/main/kotlin/com/briolink/lib/common/validation/NullOrURL.kt)                     | String must be null or valid url                     | ```validation.null-or-valid-url=Value must be null or a valid URL```                               |
| [NullOrValidUUID](src/main/kotlin/com/briolink/lib/common/validation/NullOrValidUUID.kt)         | String must be null or valid uuid                    | ```validation.null-or-valid-uuid=Value must be null or a valid UUID```                             |
| [NullOrValidWebsite](src/main/kotlin/com/briolink/lib/common/validation/NullOrValidWebsite.kt)   | String must be null or valid website                 | ```validation.null-or-website-valid=Value must be null or a valid website```                       |
| [ValidUUID](src/main/kotlin/com/briolink/lib/common/validation/ValidUUID.kt)                     | String must valid UUID V4                            | ```validation.uuid.invalid=Invalid UUID, can be specified: 12345678-1234-1234-1234-123456789012``` |
| [ValidWebsite](src/main/kotlin/com/briolink/lib/common/validation/ValidWebsite.kt)               | String must valid website (https://example.com)      | ```validation.website.invalid=Invalid website, can be specified: https://example.com```            |
| [StringInList](src/main/kotlin/com/briolink/lib/common/validation/StringInList.kt)               | String must be contains in allowed value             | ```validation.string-in-list.valid=Value must be one of the following: {0}```                      |
| [ConsistentLocalDates](src/main/kotlin/com/briolink/lib/common/validation/ConsistentDates.kt)    | 2 dates startDate must be before endDate (or null)   | ```validation.range-date.start-before-end=Start date must be before end date```                    |
| [ConsistentYears](src/main/kotlin/com/briolink/lib/common/validation/ConsistentYears.kt)         | 2 years startYear must be before endYear (or null)   | ```validation.range-year.start-before-end=Start year must be before end year```                    |

#### Examples

```kotlin
// Validate dto class
@ConsistentLocalDates(message = "user-education.dates.invalid")
data class UserEducationDto(
    @get:NullOrValidUUID(message = "user-education.university-id.invalid")
    val universityId: String? = null,
    @get:NullOrNotBlank(message = "user-education.university-name.invalid")
    val universityName: String? = null,
    @get:NullOrPastOrPresent(message = "user-education.start-date.invalid")
    override val startDate: LocalDate? = null,
    @get:NullOrPastOrPresent(message = "user-education.end-date.invalid")
    override val endDate: LocalDate? = null,
    @get:NullOrNotBlank(message = "user-education.degree.invalid")
    val degree: String? = null,
) : IBaseLocalDateRange

// controller method
fun create(input: UserEducationCreate) {
    val dto = UserEducationDto(
        universityId = input.universityId,
        universityName = input.universityName,
        startDate = input.startDate,
        endDate = input.endDate,
        degree = input.degree
    )

    try {
        createOrUpdate(dto)
    } catch (e: ConstraintViolationException) {
        BlDataFetcherExceptionHandler.mapUserErrors(e).let { print(it.message) }
    }
}

// service method
fun createOrUpdate(@Valid dto: UserEducationDto) {

}

```

If you use the ConsistentLocalDates or ConsistentYears annotation in dto, you must inherit it from the
[IBaseLocalDateRange](src/main/kotlin/com/briolink/lib/common/type/interfaces/IBaseLocalDateRange.kt)
or [IBaseYearRange](src/main/kotlin/com/briolink/lib/common/type/interfaces/IBaseYearRange.kt) interface

### GraphQL error handler

[BlDataFetcherExceptionHandler](/src/main/kotlin/com/briolink/lib/common/BlDataFetcherExceptionHandler.kt)
— A base class that implements from DataFetcherExceptionHandlerResult (graphql.execution interface)

It is enabled by default if there is a Graphql package.

This class describes the handle exception, if the exception is inherited from the interface IBlException, then the error
caused in the resolver will be issued according to the GraphQL error specification.

Static methods are also available:

```kotlin
fun mapUserErrors(cve: ConstraintViolationException): List<Error>
```

The error.message will localize the message resource

To override a class, you need to inherit from the class (BlDataFetcherExceptionHandler) and add the @Primary annotation

```kotlin
@Component
@Primary
class CustomDataFetcherExceptionHandler(localeMessage: BlLocaleMessage) :
    BlDataFetcherExceptionHandler(localeMessage) {}
```

### Beans (components)

You access the beans in the project.

[BlLocaleMessage](/src/main/kotlin/com/briolink/lib/common/BlLocaleMessage.kt)
— Component that provides the message resource

If you want to load your messages, you can add a comma-separated path to messages in the application configuration in
spring.messages.basename

```yaml
spring:
  messages:
    basename: i18n/messages, messages, i18n/validation
```

[BlServletUtils](/src/main/kotlin/com/briolink/lib/common/utils/BlServletUtils.kt)
— A component that provides validation for the request that occurs within Kubernetes between services

Example of use:

```kotlin
@DgsMutation
@PreAuthorize("@blServletUtils.isIntranet()")
fun createCompany(@InputArgument("input") createInputCompany: CreateCompanyInput)
```

[BlS3Utils](/src/main/kotlin/com/briolink/lib/common/utils/BlS3Utils.kt)
— A Component that provides the S3 service which allows you to work with files in the S3 bucket

To connect, you must add the bucket name to the application

```yaml
app:
  aws:
    s3:
      name: briolink
```

### GraphQl scalars

| Name     | Type in java            |
|----------|-------------------------|
| DateTime | java.time.LocalDateTime |
| UUID     | java.util.UUID          | 
| Year     | java.time.Year          | 

To connect, add in build.gradle.kts

```kotlin
tasks.withType<com.netflix.graphql.dgs.codegen.gradle.GenerateJavaTask> {
    packageName = "com.briolink.companyservice.api"
    language = "kotlin"
    typeMapping = mutableMapOf(
        "UUID" to "java.util.UUID",
        "DateTime" to "java.time.LocalDateTime",
        "Year" to "java.time.Year",
    )
    generatedSourcesDir = "${project.buildDir.absolutePath}/dgs-codegen"
}

```

### Utils

[BlSecurityUtils](/src/main/kotlin/com/briolink/lib/common/utils/BlSecurityUtils.kt)
— To work with authorization, get the current user Id from header authorization or check for anonymous tokens

[ImageUtils](/src/main/kotlin/com/briolink/lib/common/utils/ImageUtils.kt)
— To work with pictures, get the format or convert to png

[StringUtils](/src/main/kotlin/com/briolink/lib/common/utils/StringUtils.kt)
— To work with strings

[ColorUtils](/src/main/kotlin/com/briolink/lib/common/utils/ColorUtils.kt)
— To work with colors

[RandomUtils](/src/main/kotlin/com/briolink/lib/common/utils/RandomUtils.kt)
— Helpers to generate random values

### Other type

[BlFloatRange](/src/main/kotlin/com/briolink/lib/common/type/basic/BlFloatRange.kt)
— Class range float

[BlIntRange](/src/main/kotlin/com/briolink/lib/common/type/basic/BlIntRange.kt)
— Class range int

[BlYearRange](/src/main/kotlin/com/briolink/lib/common/type/basic/BlYearRange.kt)
— Class range year

[BlLocaleDateRange](/src/main/kotlin/com/briolink/lib/common/type/basic/BlLocaleDateRange.kt)
— Class range locale date

[BlSort](/src/main/kotlin/com/briolink/lib/common/type/basic/BlSort.kt)
— Class sort

[BlSuggestion](/src/main/kotlin/com/briolink/lib/common/type/basic/BlSuggestion.kt)
— Class suggestion

### JPA

To connect [functions](/src/main/kotlin/com/briolink/lib/common/jpa/Functions.kt) in the application properties

```yaml
spring:
  jpa:
    properties:
      hibernate:
        metadata_builder_contributor: com.briolink.lib.common.jpa.Functions
```

This package provides functions for PostgreSQL

The table shows the functions that are implemented:

| name                          | postgresql function                                                                      | 
|-------------------------------|------------------------------------------------------------------------------------------|
| fts_partial                   | ```to_tsvector('simple', ?1) @@ to_tsquery(quote_literal(quote_literal(?2)) اا ':*')```  |
| fts_partial_col               | ```?1 @@ to_tsquery(quote_literal(quote_literal(?2))                        اا ':*')```  |
| fts_query                     | ```?2 @@ to_tsquery(?1, quote_literal(quote_literal(?3))                    اا ':*')```  |
| fts_plainto_query             | ```?2 @@ plainto_tsquery(?1, ?3)```                                                      |
| fts_rank                      | ```ts_rank(?2, to_tsquery(?1, quote_literal(quote_literal(?3))              اا ':*'))``` |
| tsv                           | ```to_tsvector('simple', ?1)        ```                                                  |
| orderby_equal                 | ```?1 = ?2```                                                                            |
| array_append                  | ```array_append(?1, ?2)    ```                                                           |
| array_remove                  | ```array_remove(?1, ?2)    ```                                                           |
| array_contains_element        | ```?1 @> array[?2]    ```                                                                |
| array_contains_common_element | ```?1 && array[?2]    ```                                                                |
| int4range_contains            | ```?1 <@ int4range(?2, ?3)    ```                                                        |
| ltree_equals_any              | ```?1 <@ cast(?2 as ltree[])    ```                                                      |
| nlevel                        | ```nlevel(?1)    ```                                                                     |
| daterange_cross               | ```(?1) && daterange(?2, ?3)    ```                                                      |
| jsonb_sets                    | ```jsonb_set    ```                                                                      |
| jsonb_get                     | ```?1 -> ?2 -> ?3 etc     ```                                                            |
| array_agg                     | ```array_agg(?1)    ```                                                                  |
| concat_ws                     | ``` concat_ws(?1, ?2, ?3)   ```                                                          |
| lquery_arr                    | ```?1 ?? ?2::lquery     ```                                                              |
| arr_contains_any              | ```?1 && ?2::?3```                                                                       |
| lower                         | ```lower(?1)    ```                                                                      |
| upper                         | ```upper(?1)    ```                                                                      |

Types to hibernate:

[LTreeSetType](/src/main/kotlin/com/briolink/lib/common/jpa/type/LTreeSetType.kt)
— Unique array ltree values

[LTreeType](/src/main/kotlin/com/briolink/lib/common/jpa/type/LTreeType.kt)
— ltree type postgresql

[SetArrayType](/src/main/kotlin/com/briolink/lib/common/jpa/type/SetArrayType.kt)
— Unique array values

[PersistentEnumType](/src/main/kotlin/com/briolink/lib/common/jpa/type/PersistentEnumType.kt)
— Enum type for hibernate

[PersistentEnumSetType](/src/main/kotlin/com/briolink/lib/common/jpa/type/PersistentEnumType.kt)
— Unique array enum type for hibernate

To enable custom types, you must define the types in front of the entity

```kotlin
@TypeDefs(
    TypeDef(name = "set-array", typeClass = SetArrayType::class),
    TypeDef(name = "persist-enum", typeClass = PersistentEnumType::class),
    TypeDef(name = "persist-enum-set", typeClass = PersistentEnumSetType::class),
    TypeDef(name = "ltree", typeClass = LTreeType::class),
    TypeDef(name = "ltree-set", typeClass = LTreeSetType::class)
)
@Table(name = "investor")
@Entity
class InvestorEntity() : BaseEntity() {
    @Type(type = "ltree")
    @Column(name = "primary_industry_path", columnDefinition = "ltree")
    var primaryIndustryPath: String? = null

    @Type(type = "persist-enum")
    @Column(name = "ownership_status", columnDefinition = "ownership_status")
    var ownershipStatus: OwnershipStatusEnum? = null

    @Type(type = "persist-enum-set")
    @Column(name = "universe", columnDefinition = "varchar[]", nullable = false)
    var universe: MutableSet<UniverseEnum> = mutableSetOf()

    @Type(type = "set-array")
    @Column(name = "investor_ids", columnDefinition = "varchar[]", nullable = false)
    var investorIds: MutableSet<String> = mutableSetOf()
}
```

To make enum work, it is necessary to inherit
from [PersistentEnum](/src/main/kotlin/com/briolink/lib/common/jpa/type/PersistentEnumType.kt)
'value' will be recorded in the database

```kotlin
enum class OwnershipStatusEnum(val title: String, override val value: String) : PersistentEnum {
    PrivatelyHeldBacking("Privately Held (backing)", "privately_held_backing"),
    PrivatelyHeldNoBacking("Privately Held (no backing)", "privately_held_no_backing"),
    InIpoRegistration("In IPO Registration", "in_ipo_registration"),
    PubliclyHeld("Publicly Held", "publicly_held"),
    AcquiredMerged("Acquired/Merged", "acquired_merged"),
    AcquiredMergedOperatingSubsidiary("Acquired/Merged (Operating Subsidiary)", "acquired_merged_operating_subsidiary"),
    OutOfBusiness("Out of Business", "out_of_business");

    companion object {
        val map = values().associateBy(OwnershipStatusEnum::title)
        private val mapValues = values().associateBy(OwnershipStatusEnum::value)
        fun fromName(name: String): OwnershipStatusEnum =
            map[name] ?: throw IllegalArgumentException("Unknown value $name")
        fun fromValue(value: String): OwnershipStatusEnum =
            mapValues[value] ?: throw IllegalArgumentException("Unknown value $value")
    }
}
```