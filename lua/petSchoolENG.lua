--[[Falta inserir a linha pra direcionar para
petSchool1, petSchool2, petSchool3 de acordo
com o horario do jogador.
PS: como faz pro petTime rastrear o horario do jogador?
PS2: revisar se o goto funciona
PS3: como adicionar o mod de atributos?
--]]
hourOfDay = pet:w():hourOfDay() --tem q linkar com o horario do jogo

petName = '[school] Pingo' -- provisorio ate ter o codigo que o jogador nomeia o personagem

math.randomseed(os.time())
--[[eventos que acontecem entre 08:00AM to 12:00PM
--]]

if hourOfDay >= 08 or
   hourOfDay <= 12 then
	petSchool1 = math.random(1,20)
	if petSchool1 == 1 then
	print(petName, "started a good friendship.")
	end
	if petSchool1 == 2 then
	print(petName, "was invited to a birthday party.")
	end
	if petSchool1 == 3 then
	print(petName, "joined the NERDS.")
	end
	if petSchool1 == 4 then
	print(petName, "was part of a science fair.")
	end
	if petSchool1 == 5 then
	print(petName, "is doing pretty good in art class.")
	end
	if petSchool1 == 6 then
	print(petName, "solved a math question in the board.")
	end
	if petSchool1 == 7 then
	print(petName, "joined in a RPG group.")
	end
	if petSchool1 == 8 then
	print(petName, "was invited to study in a friend`s house.")
	end
	if petSchool1 == 9 then
	print(petName, "stoled a kiss and was spanked.")
	end
	if petSchool1 == 10 then
	print(petName, "had a fight.")
	end
	if petSchool1 == 11 then
	print(petName, "raised skirt from a PRETTY GIRL.")
	end
	if petSchool1 == 12 then
	print(petName, "was busted walking naked around the school.")
	end
	if petSchool1 == 13 then
	print(petName, "started the fire extinguisher.")
	end
	if petSchool1 == 14 then
	print(petName, "called bad names to the TEACHER.")
	end
	if petSchool1 == 15 then
	print(petName, "started a paper war.")
	end
	if petSchool1 == 16 then
	print(petName, "was busted talking during the class.")
	end
	if petSchool1 == 17 then
	print(petName, "reported a friend cheating in the test.")
	end
	if petSchool1 == 18 then
	print(petName, "suffered bullying.")
	end
	if petSchool1 == 19 then
	print(petName, "was busted stealing in the school.")
	end
	if petSchool1 == 20 then
	print(petName, "was busted starting fireworks in the bathrooms.")
	end
	io.read();
elseif hourOfDay > 12 or
       hourOfDay <= 17 then
	--[[eventos que acontecem entre 12:00PM to 17:00PM
	--]]
	petSchool2 = math.random(1,20)
	if petSchool2 == 1 then
	print(petName, "received good feedback from the TEACHER.")
	end
	if petSchool2 == 2 then
	print(petName, "was ignored by the BULLIES.")
	end
	if petSchool2 == 3 then
	print(petName, "was kissed by the CHUBBY GIRL.")
	end
	if petSchool2 == 4 then
	print(petName, "was flirted by someone.")
	end
	if petSchool2 == 5 then
	print(petName, "wrote a funny text.")
	end
	if petSchool2 == 6 then
	print(petName, "shit during the class.")
	end
	if petSchool2 == 7 then
	print(petName, "farted during English class.")
	end
	if petSchool2 == 8 then
	print(petName, "pissed on his pants.")
	end
	if petSchool2 == 9 then
	print(petName, "glued TEACHER`s chair.")
	end
	if petSchool2 == 10 then
	print(petName, "throwed a paper ball on the TEACHER.")
	end
	if petSchool2 == 11 then
	print(petName, "got high with candies in breaktime.")
	end
	if petSchool2 == 12 then
	print(petName, "was charged to be a nerd from BACKROOM GUYS.")
	end
	if petSchool2 == 13 then
	print(petName, "was charged to be a nerd from BACKROOM GUYS.")
	end
	if petSchool2 == 14 then
	print(petName, "slept during History class.")
	end
	if petSchool2 == 15 then
	print(petName, "ate a rotten food.")
	end
	if petSchool2 == 16 then
	print(petName, "barfed during a question.")
	end
	if petSchool2 == 17 then
	print(petName, "lost a friend.")
	end
	if petSchool2 == 18 then
	print(petName, "was beaten up by the BACKROOM GUYS until bleeds.")
	end
	if petSchool2 == 19 then
	print(petName, "did not studied for the test. Just signed his name.")
	end
	if petSchool2 == 20 then
	print(petName, "kicked the TEACHER. By mistake.")
	end
	io.read();
elseif hourOfDay > 17 or
       hourOfDay <= 22 then
--[[eventos que acontecem entre 17:01PM to 22:00PM
--]]
	petSchool3 = math.random(1,20)
	if petSchool3 == 1 then
	print(petName, "was busted doing a PRETTY GIRL in the bathroom.")
	end
	if petSchool3 == 2 then
	print(petName, "escaped from be mugged backing home.")
	end
	if petSchool3 == 3 then
	print(petName, "touched by mistake in PRETTY GIRLs tits.")
	end
	if petSchool3 == 4 then
	print(petName, "became friend from BACKROOM GUYS.")
	end
	if petSchool3 == 5 then
	print(petName, "sneezed in the PRETTY GIRL.")
	end
	if petSchool3 == 6 then
	print(petName, "farted and shit himself.")
	end
	if petSchool3 == 7 then
	print(petName, "is being called fart knocker.")
	end
	if petSchool3 == 8 then
	print(petName, "had some alcohool with BACKROOM GUYS.")
	end
	if petSchool3 == 9 then
	print(petName, "was found in the garbage.")
	end
	if petSchool3 == 10 then
	print(petName, "slept during class and woke up naked.")
	end
	if petSchool3 == 11 then
	print(petName, "was part in a violent fight.")
	end
	if petSchool3 == 12 then
	print(petName, "brought a hobo to the school.")
	end
	if petSchool3 == 13 then
	print(petName, "sat in a rusty pin.")
	end
	if petSchool3 == 14 then
	print(petName, "offered fake scotch to his friends.")
	end
	if petSchool3 == 15 then
	print(petName, "burped during his presentation.")
	end
	if petSchool3 == 16 then
	print(petName, "accepted drugs from a friend.")
	end
	if petSchool3 == 17 then
	print(petName, "had a friend that died by overdose.")
	end
	if petSchool3 == 18 then
	print(petName, "sold drugs.")
	end
	if petSchool3 == 19 then
	print(petName, "was busted smelling strange stuff behind the cantina.")
	end
	if petSchool3 == 20 then
	print(petName, "cripped a friend during a fight.")
	end
	io.read();
end
