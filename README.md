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

This table provides [annotations](/src/main/kotlin/com/briolink/lib/common/validation) that validate the class and causes a ConstraintViolationException if the validation is broken.

| Annotation class | Description | Default message |
| ---------------- | ----------- | --------------- |

### GraphQL error handler

[BlDataFetcherExceptionHandler](/src/main/kotlin/com/briolink/lib/common/BlDataFetcherExceptionHandler.kt)
— A base class that implements from DataFetcherExceptionHandlerResult (DGS component)

It is enabled by default if there is a DGS package.

### Beans (component)

You access the beans in the project.

[BlLocaleMessage](/src/main/kotlin/com/briolink/lib/common/BlLocaleMessage.kt)
— Annotation checks if the user has rights to use the function

[BlServletUtils](/src/main/kotlin/com/briolink/lib/common/utils/BlServletUtils.kt)
— Annotation checks if the user has rights to use the function

### Table rights

| Name right         | AccessObjectType | Description                                              |
|--------------------|------------------|----------------------------------------------------------|
| EditOwner          | Company          | Can assign owners and remove them                        |
| EditAdmin          | Company          | Can assign admins and remove them                        |
| EditSuperuser      | Company          | Can assign Superusers and remove them, edit their rights |
| EditCompanyProfile | Company          | Can edit setting and info about company                  |
| EditEmployees      | Company          | Can view the admin panel. Accept new employees           |
| EditProject        | Company          | CRUD Project in Company profile                          |
| EditCompanyService | Company          | CRUD All services in Company                             |
| EditNeedsExchange  | Company          | CRUD Needs                                               |
| CreateProject      | Company          | Can create projects without confirmation                 |
| EditConnection     | Company          | CRUD All connection in Company                           |
| EditWidget         | Company          | CRUD All connection in Company                           |

### Basic classes

[AllowedRights](https://gitlab.com/briolink/network/backend/permission-lib/-/blob/main/src/main/kotlin/com/briolink/lib/permission/AllowedRights.kt)
— Annotation checks if the user has rights to use the function

In function must be contained accessObjectId - String

[PermissionService](https://gitlab.com/briolink/network/backend/permission-lib/-/blob/main/src/main/kotlin/com/briolink/lib/permission/service/PermissionService.kt)
— Main service

[UserPermissionRights](https://gitlab.com/briolink/network/backend/permission-lib/-/blob/main/src/main/kotlin/com/briolink/lib/permission/model/UserPermissionRights.kt)
— Model about rights and role by user

[UserPermissionRole](https://gitlab.com/briolink/network/backend/permission-lib/-/blob/main/src/main/kotlin/com/briolink/lib/permission/model/UserPermissionRole.kt)
— Model about rights for the object

## Examples

### Annotation AllowedRights

If more than one value is specified in value, the function will execute when the user has one of these rights

When the user wants to update the company logo, the user must have the right IsCanEditCompanyProfile

```kotlin
    @AllowedRights(value = ["EditCompanyProfile@Company"], argumentNameId = "id")

fun uploadCompanyImage(
    @InputArgument("id") id: String,
    @InputArgument("image") image: MultipartFile?
): URL {
    return companyService.uploadCompanyProfileImage(UUID.fromString(id), image)
}
```

If User haven`t right this example will return
exception [AccessDeniedException](https://gitlab.com/briolink/network/backend/permission-lib/-/blob/main/src/main/kotlin/com/briolink/lib/permission/exception/AccessDeniedException.kt)

### Add permission role

Sets the role for a user with default rights

```kotlin
try {
    permissionService.createPermissionRole(
        userId = userId,
        accessObjectType = AccessObjectTypeEnum.Company,
        accessObjectId = companyId,
        permissionRole = PermissionRoleEnum.Owner,
    )?.also {
        updateUserPermission(userId, companyId)
    }
} catch (_: PermissionRoleExistException) {
}
```

### Check permission right

EditCompanyService@Company

- EditCompanyService - this action
- Company - this access object type

You see more in the table rights

```kotlin
if (permissionService.checkPermission(
        userId = SecurityUtil.currentUserAccountId,
        accessObjectId = UUID.fromString(companyId),
        right = "EditCompanyService@Company"
    )
) updateCompanyService(name = "New company service")
```