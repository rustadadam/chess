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
- What are the objects
- How do the objects interact with the obejects?
- We only need to include things that our domain needs to know. 
- SIMPLICITY is important!!

### Abstraction and Encapsulation
- It is key to Domain Driven Design
- Helps create classes that can be created in different ways. 
- Encapsulation is like wrapping them... you "Has-a"
- Inheritance is like "Is-a

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
