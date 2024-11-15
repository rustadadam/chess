# This is just a trial

Will it work? I crossed my fingers.

## How to add a file to git via terminal

echo "# My notes" > notes.md
git add notes.md
git commit -am "initial(notes) creation"
git push

# Phase 0 Notes

## Java Details

- Java compiles into a bytecode, which is portable. It needs to be read.
  It is like an intermediated complier.
- It needs to be compiled. Running java file.java will compile then run it.
- It reads and works very similar to C++.
- For loops have iterables
- Enum is like a class that has a bunch of functions built in
- Java can only inherit one class at a time. If none is specified, it will inherit from object. Uses the extend keyword.
- '==' means it compares the memory location between objects
- Record creates a class that overides the hashCode, to_string, and equals

## Programming tips
- Make everything private unless something else needs to know. (Create classes like a need-to-know basis)
- Overide the following functions: to_string, hashCode, equals

### Terms
- Polymorphism: Can be related in many forms
- Interface: It is a class that has certain values without the implementation. This allows for different implementations for different things.
  - Implements is the keyword to use the interface
- instanceof checks to see if th object you give it is the object listed.

###  Packages
- To be able to compare objects, you will need to implement the comparable interface. It will tell you if things are greater or less than


# Phase 1 Notes
## DDD (Domain Driven Design)
- What are the actors?
- What tasks do the actors want to acomplish
- What are the objects the actors use?
- How do the objects interact with the actors?
- We only need to include things that our domain needs to know. 
- SIMPLICITY is important!!

### Abstraction and Encapsulation
- It is key to Domain Driven Design
- Helps create classes that can be created in different ways. 
- Encapsulation is like wrapping them... you "Has-a"
- Inheritance is like "Is-a
- Cohesian is like "I only do those things I need to do"

## Simplicity
- Decomposition: Its like one bite at a time... splitting a big project into littler pieces
- KISS: "Keep it simple Stupid"
- YAGNI: "Your are not going to need it"
- DRY: "Dont repeat yourself"
- Additionally, you want high cohesian but low coupling
- POLA: Principle of least Astonishment

## SOLID
### Single Resposibility
- For each object, the actor only has one reason to use that object
### Open Closed
- It means that its open to be extended, but closed to being changed. 
- This allows for reuse
### Liskov Substition
- This is the idea that you can change any class that implementing something and subsitute it with another class that also implemetned it and won't crash.
### Interface segregation
- You break interfaces into parts
### Dependency inversion
- Don't need things on a route or like cars. Instead of defining within class, you pass things in.

## Classes Lecture
- static keyword on a class definition means it can be called outside the class, and that it knows nothing about the class its in. Its like it was in another file
- singleton is a design structure that allow allows one object of a class
- Any inner class without the keyword static can reference parts of the class
- Closure is the term is that an inner class can reference variables on the stack -- within the scope. It remembers the creation state of when it was made
- Anonymous classses are new interface -- (its like a one time use class)
- Lambda is like an anymous class (or method) with closure
- A functional interface only has one method
- lambda functions are like this (a,b) -> a + b

## IO stream
- It follows first in, first out. 
- Think of it like a stream.

## Gemerics
- Is a way to create arrays, and you can restrict the array. Therefore an new ArrayList<Integer>(); can only hold integers

## Serialization
- It takes objects and makes them something writeable. 
- json is an example of a serialized file
- yml is another one like json that uses whitespace. 
- json stores objects and is like a map
- gson (goodles version) unserializes json 

## UML
- You can group, create players, and objects


# Project 3
## HTTP (hyper text transport protocol)... if with an s means secure
- Client to server protocol means Client has to talk first.
- They have a domain name and a port. The domain name is like the costal city, and port is the peir
- URL stands for Uniform Resource Locator
- HTTP uses requests. It has a method (post), path, version. It then has a bunch of headers to make it a map

## Code Quality
- Goals of Design at the lowest level: It works today, and it will work tomorrow
- Goals of Quality: 1. Can I understand it today and tomorrow, quickly at the right level? Can I enhance it?
- We want to follow the conventions of the team we are in 
- Simplicity over clever or concise
- Documentation isn't as good as clear code, but is helpful. Its like having the code twice. 
- Helpful concepts: Use Symbols, Reduce Parameters, Be consistent, and single return
- Refactoring: change code that doesn't affect its runtime. 

## Test Driven Development (TDD)
- Tests help everything
- @ParameterizedTest checks multiple parameter sets. 

# SQL
- Its a declaritve language gives the intent
- Imperative language would be like Java

## MySQL
- MySQL implements SQL
- When you create a table, you have to set a key to primary. It has to be unique. 
- You can make the speed of searching on type, you can index it --> which significantly speeds up the query
- https://github.com/softwareconstruction240/softwareconstruction/blob/main/instruction/db-sql/example-code/initialize.sql
- Create database with database manager

## Command Line Interface (CLI)
- Git is an example of a database
- REPL is Read Evaluate Print Loop
- echo -e "\u001b[31;40;7m ♔♕♖♗♘♙♚♛ "
- URI is a Universal --- Identifier

## Logging
- External recording of your program
- Helps with debugging, and mapping. Sometimes is the only way to debug. The debugger may be better.
- Logging properties are helpful: Persistent, Immutable, Aggregated, Accessible, Performat
- Logger objects can handle multiple levels of logging. 
- We can make custom handlers 

## HTTP Highlights
- Client initiates, Server responds
- Extensive Caching
- Methods, paths, headers
- Only 1 directional

## Websocket
- Its bidirectional between a server and something else
- Automatically detects whether it is dropped or not

# Final Notes
- We are going to need to read the json to detemine the type of a USerGame command
- You don't need to make a leave, resign, and observer classes.
- We can use the terminal for multiple uses
- Use websocket to repass the html commands

# Additional Notes
## Cryptograph
- Authentication: who am I. Authorization: What can I do. Data-Integrity: Non-changing. Non-Repudiation: Ownership
- SHA-256 is standard today, and Bcrypt is too but is also way slower
- collisons means that two objects map to the same thing

## Encryption and Decryption
- AES : never broken. Standard method for today
- Super easy to do, and very helpful
- Symmetric key dycryption: Easy, but hard to distrubtion
- Asymmetric key dycryption: Uses a secret key and public key
- Private key can dycrypt what is encrypted through a public key, but a public key cannot do it
- The standard use is ECC for .
- Disadvantage: Slow, size-restriction. Advantage: Open key distribution
- Certificate Authorities provide a web certificate which help people learn
