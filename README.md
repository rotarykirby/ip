# Lebron
> "If my mom had balls she'd be my dad"  – Max Verstappen ([source](https://www.youtube.com/watch?v=Nq82CB-zOto))

We all know who Lebron is. He is not only the cutest pookie **and** the greatest player of all time, he is now also your personalised chat bot that is:
- Free
- Open-source
- Lightning fast


All you need to do the get your own Lebron is,
1. download him from [here](https://github.com/rotarykirby/ip/releases/tag/A-Jar)
2. double-click it.
3. add your tasks.
4. let it manage your tasks for you 😉

And you can then enjoy your very own Lebron!

Features:
- [x] Keep track of your to do tasks, deadlines, and events
- [x] Mark tasks as complete/incomplete
- [x] Check what deadlines/events occur on a specific date


---
Note for Java programmers: you may change the folder where task's data is stored by changing the string in the `main` method of the Lebron class as shown below:
```java
public class Lebron {
    public static void main(String[] args) {
        new Lebron("./data/Lebron.txt").run();
    }
}
```
