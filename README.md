# Robot Template

A work-in-progress project which can be cloned as a base for all future robots. This project provides a consistent
foundation for the Team 1251 projects along with documentation on related practices and patterns.

There are `README.md` files in each of the packages which talk more about how the package is to be used.

## Dependency Injection vs. Static Access

The WPI Lib, Command Based programming documentation relies heavily on making `Subsystems`, `Commands`, and other 
components statically accessible. We have been able to identify some apparent benefits to relying so heavily on static
access:
 
  - Does not require explanation of dependency injection as a concept to beginner students
  - Makes the code look simpler
  - Works around a ["chicken or egg"](https://en.wikipedia.org/wiki/Chicken_or_the_egg) problem between 
    `Subsystems` and their default `Commands`.

However, Team 1251 uses [dependency injection](https://en.wikipedia.org/wiki/Dependency_injection) over static access
as a matter of practice after a closer review of static access benefits. The thought process leading to that decision
is documented below. Be warned -- it is abridged, but it _is_ a thought *process*.

_Before continuing, I think it is important to highlight that we make no judgements or assumptions about WPILib 
contributors in terms of favoring static access in the official documentation. There may be very legitimate benefits
which we have not yet considered. Feedback is always welcome!_

> Does not require explanation of dependency injection as a concept to beginner students

This is the hardest point to argue with; dependency injection can be a difficult and involved topic. However, it is
an important programming concept -- *especially* with the adoption level of functional programming in the professional 
programming space.
 
Our general viewpoint that understanding dependency injection is an important skill for programming students to learn; 
at the very least we want the concept to seem familiar to them as they explore code in the wild.

> Makes the code look simpler

This is very closely related to the previous point. It undoubtedly does _look_ simpler, but is it?

There are just as many dependencies either way. However, with static access it is much harder to reason about and to
predict the order in which various components become available. The order of instantiation becomes a matter of which 
order the static properties are defined in. Before switching to dependency injection, we found ourselves rearranging 
our properties because instances were not ready when we needed them to be -- a detail which was not apparent until the
code is executed :-|  We found the code to be very fragile and very easy to break without realizing it.

Dependency injection has the benefit of making the dependency tree _painfully_ obvious. It is apparent exactly how 
instances flow from one place to the next. Moreover, the IDE can help you identify use-before-initialization errors
with dependency injection. It isn't foolproof, but this this just isn't an option with static access. 

The obvious trade off to all this is that is that you *have* to push the instances around and that means more code to 
wire things together. For us, the trade off is worth having code that is less fragile and more "obvious".

> Works around a "chicken or egg" problem between `Subsystems` and their default `Commands`.

So here we come to the technical challenge with static access and Command Based programming...

The default `Command` of a `Subsystem` must require that `Subsystem`... So the default command can not be injected into
the constructor of the subsystem because the Subsystem does not yet exist to be required!  This is unfortunate.

The prescribed way to apply the default command is to implement `Subsystem.initDefaultCommand()` and use it to reach out 
into that statically accessible, global-ish space to assign the default command with `Subsystem.setDefaultCommand()`.
According to the JavaDocs, the `initDefaultCommand()` method "... is called on all Subsystems by CommandBase in the 
users program after all the Subsystems are created."

The actual call chain of `initDefaultCommand()` is hard to follow. It looks like it is called once, if it hasn't been
called before when `getDefaultCommand()` is called. Digging deeper, `getDefaultCommand()` is called in two places:

  - Every time the `Scheduler` runs -- if there isn't another command in control of the `Subsystem`
  - When the name of the default command is sent over network tables
  
With this in mind, so long as the default command is set before you need to see the name in a dashboard and before it
needs to run, you're fine! So it turns out that it is as simple as:

```java
DriveTrain driveTrain = new DriveTrain()
TeleopDrive teleDrive = new TeleopDrive(driveTrain) // dependency injection!
driveTrain.setDefaultCommand(teleDrive)
```

And *that* code snippet really sums up why we use Dependency Injection.  It is obvious, at a glance, that the `TeleOp`
drive is dependent on the `DriveTrain` and they are all created in a predictable, easy to understand order.