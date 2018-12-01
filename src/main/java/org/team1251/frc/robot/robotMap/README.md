# robot.robotMap

The name of this package is derived from the `RobotMap` class which comes with the default WPI Lib robot template 
because it focuses on the same goal:

  Making it easy to manage device assignments as the robot evolves.
  
It usually has exactly two files in it:

   - Devices (enum)
   - RobotMap


A stub of the `Devices` and a usable version of `RobotMap` are provided in this project. These files are designed to 
work in concert with `org.team1251.frc.robotCore.robotMap.*` (referred to as `robotCore.robotMap` later in this doc
for brevity). More information about each can be found below as well as the JavaDoc of each.
   

## Devices (enum)

This enum simply lists all devices and the port assignment of each. Each enum value implements the 
`robotCore.robotMap.Device` interface allowing it to work with `robotCore.robotMap.DeviceManager`.

## RobotMap

This class uses the classic name for familiarity. It simply provides static access to an instance of
`robotCore.robotMap.DeviceManager` and automatically looks for duplicate port assignments as soon as the class is
loaded. 