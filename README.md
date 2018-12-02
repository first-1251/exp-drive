# Experimental Drive Systems

This project is a test-bed for experiments with various drive control schemes.

## Control Options

The controller style is selectable through the `Drive Controls` Smart Dashboard 
chooser while the Robot is disabled.

### Arcade: Dual Stick

Controls:
- **Forward:** `Left Stick` Up
- **Backward:** `Left Stick` Down
- **Turn:** `Right Stick` Left/Right

### Arcade: Trigger Throttle

- **Drive Forward:** `Right Trigger`
- **Drive Backward:** `Left Trigger`
- **Turn:** `Left Stick` Left/Right 

*Note: Using both `RT` and `LT` will cause them to fight one another. The final speed 
and direction will be determined by how hard each button is being pressed. For example,
75% press of `RT` and 100% press of `LT` will result in 25% backward speed.*

### Tank: Dual Stick

- **Right Side Throttle:** `Right Stick`
- **Left Side Throttle:** `Left Stick`