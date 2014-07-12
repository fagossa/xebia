# What is mowitnow ?

This is just an example implementation of a robot executing a list of instructions in the way logo did back in the day. Our robot (called mower) is placed is a square surface with strict limits that cannot be crossed. 

# supported instructions

What are the supported instructions?

* **D**: turn 90° to the right
* **G**: turn 90° to the left
* **A**: advance one step in the current direction

# How does it work ?

You have to instantiate the **SurfaceController** and specify instructions you want to execute. You can do it either by using an String array or a file containing all the instructions.

By convention the first instruction is the area or surface in which the different mowers can be moved. The following instructions must be first the mower's start position and then the list of instructions to execute in a sequence.

For example, this code:

    SurfaceController controller = new SurfaceController();
    String[] movements = {
        "5 5",
        "1 2 N",
        "GAGAGAGAA",
        "3 3 E",
        "AADAADADDA"
    };
    SurfaceDto surface = controller.executeMovements(movements);
    List<MowerDto> list = surface.getMowers();


means:

* There is a surface of 5x5 
* The first mower starts at (1,2) facing **North**
* The second mower start at (3,3) facing **East**

At the end of the processing it is expected that the final locations be:

* (1, 3) facing **North** for the first mower and
* (5, 1) facing **East** for the second one