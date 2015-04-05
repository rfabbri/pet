-- simple script to print the game time and other info continuously

-- bind to classes we need
--local w = luajava.bindClass("com.pulapirata.core.PetWorld")
--local w = luajava.bindClass("com.pulapirata.core.PetWorld")

--print "[lua] test tst.lua"


--print " *** experimenting with enum interface for attributes check *** "
a=pet:a()

--print("now: ", type(a:sNutricao():getState()))

s = a:sNutricao():getState()

--print("this: ", type(s))

--print("here: ", a:sNutricao():getState())

--print("testicle: ", a:sNutricao():get(), type(a:sNutricao():get()))

--print("attr: ", a:nutricao())
--print("attr get: ", a:nutricao():get(), type(a:nutricao():get()))


-- remains:
--
-- 1) loop over all available sAttributes
--      - need to be done using map.


-- k = a:sAtt()
--print("haha: " , type (a:sAtt(k))
--print("num atributos com estado qualitativo: " , a.ms_:size())

numKeys = a.ms_:size()

keys = a.ms_:keySet():toArray()

--print("haha: " , type (keys))
local Array = luajava.bindClass("java.lang.reflect.Array")

--for i = 0,  numKeys-1 do
--  print("hAhA: " , Array:get(keys,i))
--end
-- see enum.lua for an initial set of helper functions for enum + lua


--print " Done experimenting with enum interface for attributes check *** "

-- print ("Beat: ", w:beat_);
-- print ("Idade (coelho minutos): ", w:idadeCoelhoMinutos());
--print ("BLA : ", w.PET);
