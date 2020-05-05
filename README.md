# JavaTimecode
MTC and LTC timecode generator library in Java

**NOTE:** The library is still a wip.

## What's working:
- LTC generator
- Timecode data managment 
- Timecode set fields
- TimecodePlayer to choose timecode offset + speed (and, consequently, also direction)

## TODO list:
- ALL of the MTC part
- Write a timecode reader for both MTC and LTC
- 44.1khz support (But I think this will be done together with the previous point)
- Writing docs (sigh)
- Writing an example/test

### The idea

After searching A LOT for some free and open source LTC libraries for java, I've founded [this one](https://github.com/MrExplode/ltc4j) that sounded interesting. I take a look at the code, and I noticed it wasn't working well + there were a lot of stuff that could be improved. So I decided instead to start from scratch and write my own library that will handle both LTC and MTC, by using some ideas I've found in that library. Also, I've asked [Andrea Colla](https://github.com/AndColla) (A friend of mine) for some help, because, sadly, I'm quite busy rn.
