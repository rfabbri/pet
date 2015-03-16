-- Main script to control Pet
--
-- Access and modify the game using any public methods and variables of the Pet
-- class from Pet.java
--
-- Examples
--
--   print( pet:w():hourOfDay() )                 -- access the hour of day (24h)
--
--   pet:w().tAverageDuracaoPuloAleatorio_ = 2    -- change the average jump duration
--
--
-- The global variable "pet" is mapped to the Pet class (Pet.java)
-- In Pet.java, we can see that w() of the PetWorld class (PetWorld.java)
-- Inside PetWorld.java, we can see that hourOfDay() is public and can thus be
-- called.
--
-- Methods are called using ":" and members using "."
--
-- Use dofile() to run your other lua files and modularize the code
--
-- This script is run by the Pet game at every update loop
-- It is called at (Pet.update() in Pet.java)
--

print("HHSHSHHSHSHSHSHSHS")

dofile("lua/tst.lua")
