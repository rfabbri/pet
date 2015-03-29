-- simple script to print the game time and other info continuously

-- bind to classes we need
--local w = luajava.bindClass("com.pulapirata.core.PetWorld")
--local w = luajava.bindClass("com.pulapirata.core.PetWorld")

print "[lua] test tst.lua"


print " *** experimenting with enum interface for attributes check *** "
a=pet:a()

print("now: ", type(a:sNutricao():getState()))

s = a:sNutricao():getState()

print("this: ", type(s))

print("here: ", a:sNutricao():getState())

print("testicle: ", a:sNutricao():get(), type(a:sNutricao():get()))

print("attr: ", a:nutricao())
print("attr get: ", a:nutricao():get(), type(a:nutricao():get()))

print " Done experimenting with enum interface for attributes check *** "

-- print ("Beat: ", w:beat_);
-- print ("Idade (coelho minutos): ", w:idadeCoelhoMinutos());
--print ("BLA : ", w.PET);
