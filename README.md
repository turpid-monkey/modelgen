#modelgen

Creates rich data model code from annotated interfaces. Uses velocity. Work in Progress.

## Introduction

Lots of the code in a data model is boiler plate code. E.g. getter/setter-Methods, handling of containment, factory methods, type validation, visitor implementation, commands. There are various frameworks that handle code generation and create rich data model code, but those frameworks usually introduce heavy-weight dependencies and/or use separate model definition languages like ECore, Schema oder UML.

This framework tries to stick with Java interfaces (plus annotations) for data model specification, and to generate Java code with no or very light-weight dependencies.

The framework provides a basic set of velocity-based templates and can be integrated in a gradle build.

## Planned Features

* Generates getter/setter methods for a given getter interface method
* Generates container/containment code
* Generates visitor implementations that recursively visits all non-primitive, typed members of the data model
* Generates command code with support for undo/redo
* Generates basic validation code that checks model instances for required properties and proper containment
* Generates factory methods that allow/force the creation of valid model instances
* Works as gradle plugin/task

## Feature Description

### Supported Annotations

|Annotation | Level | Description | Generated Code|
|-----------|-------|-------------|---------------|
|`@Abstract`|interface|Generated model type implementation remains abstract. | type implementation: `abstract class XyzImpl implements Xyz ...`|
|`@Required`|getter method|Valid instances of the model type need to have non-null values for the marked properties| validation code, factory method |
|`@Containment`|getter method|Defines a parent/child relation between model type and property. The child interface needs to implement `Contained<parent type>` interface.| type implementation: set/unset parent reference, child implementation: accordingly generate Contained code, validation and factory. |
|`@Types(<list of model types>)`|getter method|For abstract model type properties, limit the amount of available types (by default all)| type implementation: generate typed setters|
|`@Custom(<invocation handler>)`|any method|Provde a custom implementation for the annotated method, e.g. a java.lang.reflect.InvocationHandler| type implementation: delegates call to invocation handler |

#### `@Abstract` annotation
Usage:
```java
@Abstract interface LocatableCode {
   CodeLocation getLoc();
}
@Abstract interface Identity {
   ID getID();
}
interface Code implements LocatableCode, Identity {
   
}
```
Code is only generated for non-abstract type `IdenticalCode`, the code of the abstract interfaces gets added. 
```java
public abstract class CodeObject implements Code {
    ID id; CodeLocation loc;
    // getter/setter-methods
}
```

#### `@Required` annotation
Usage:
```java
interface Person {
    @Required String getName();
}
```
The type `Person` gets a constructor with all required properties as parameter, validation code will check whether required properties are null, additionally a factory method is added to a `ModelFactory` class.

```java
public class ModelFactory {
    public static Person createPerson(String name);
}

public class PersonObject implements Person {
    String name;
    public PersonObject() {}
    public PersonObject(String name) { this.name = name; }
    // getter/setter
}

public class PersonValidator1 implements Validatelet<Person> {
    public void validate(Person p, ValidationContext ctx) {
       if(p.getName()==null) ctx.error("Person.name may not be null");
    }
}
```

