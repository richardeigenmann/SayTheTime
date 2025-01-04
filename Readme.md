# SayTheTime

A JAVA program that says the time in words

## Simplest use case: Output onto the Command Line

After installing the program, run it on the command line and 
it will say the current time. Here is a Linux BASH example:

```bash
user@computer:~> /richi/Src/SayTheTime/build/image/bin/SayTheTime
Es isch fascht Mittag
```

Or specify the time to say:
```bash
user@computer:~> /richi/Src/SayTheTime/build/image/bin/SayTheTime 13:15
Es isch viertel ab eis
```

## Use it with cowsay

On Linux you can install the program cowsay and use that to let the cow say the time:

```bash
user@computer:~> /richi/Src/SayTheTime/build/image/bin/SayTheTime 13:15 | cowsay
 ________________________ 
< Es isch viertel ab eis >
 ------------------------ 
        \   ^__^
         \  (oo)\_______
            (__)\       )\/\
                ||----w |
                ||     ||
```

Cowsay has many options. Check them with `man cowsay` For instance, to have a 
stoned cow say the time:

```bash
user@computer:~> /richi/Src/SayTheTime/build/image/bin/SayTheTime 13:15 | cowsay -s
 ________________________ 
< Es isch viertel ab eis >
 ------------------------ 
        \   ^__^
         \  (**)\_______
            (__)\       )\/\
             U  ||----w |
                ||     ||
```

In the above examples I used the shells pipelining feature to send the Standard Output 
of the SayTheTime command to the next command (`cowsay`) as Standard Input. The pipe 
character (`|`) did that for us.

Alternatively we can also pass the time from `SayTheTime` as an argument to 
the `cowsay` program. We can ask the shell to run a command for us with the
`$()` syntax (or backtick, which I find less readable) and then take
the output of that command and pass it as an argument
tp the `cowsay` program. Here with the ghostbusters 'cow':

```bash
user@computer:~> cowsay -f ghostbusters $(/richi/Src/SayTheTime/build/image/bin/SayTheTime)
 __________________________________ 
< Es isch foif über halbi eis gsi >
 ---------------------------------- 
          \
           \
            \          __---__
                    _-       /--______
               __--( /     \ )XXXXXXXXXXX\v.
             .-XXX(   O   O  )XXXXXXXXXXXXXXX-
            /XXX(       U     )        XXXXXXX\
          /XXXXX(              )--_  XXXXXXXXXXX\
         /XXXXX/ (      O     )   XXXXXX   \XXXXX\
         XXXXX/   /            XXXXXX   \__ \XXXXX
         XXXXXX__/          XXXXXX         \__---->
 ---___  XXX__/          XXXXXX      \__         /
   \-  --__/   ___/\  XXXXXX            /  ___--/=
    \-\    ___/    XXXXXX              '--- XXXXXX
       \-\/XXX\ XXXXXX                      /XXXXX
         \XXXXXXXXX   \                    /XXXXX/
          \XXXXXX      >                 _/XXXXX/
            \XXXXX--__/              __-- XXXX/
             -XXXXXXXX---------------  XXXXXX-
                \XXXXXXXXXXXXXXXXXXXXXXXXXX/
                  ""VXXXXXXXXXXXXXXXXXXV""
```

## The Qlocktwo inspiration

Having admired the beautiful Qlocktwo wall clocks made by 
Biegert & Funk (www.qlocktwo.com ) I felt like doing something similar with words.

![Image of the Qlocktwo clock](https://de.wikipedia.org/wiki/Datei:Qlocktwo_Touch_mit_rotem_Frontcover.JPG)

Side note: check out this cool TCL implementation of the Qlocktwo https://wiki.tcl-lang.org/page/QLOCKTWO+in+Tcl


