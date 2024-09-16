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
