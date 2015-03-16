--[[Falta inserir a linha pra direcionar para
petPark1, petPark2, petPark3 de acordo
com o horario do jogador.
PS: como faz pro hourOfDay rastrear o horario do jogador?
PS2: revisar se o goto funciona - done
PS3: como adicionar o mod de atributos?
--]]

hourOfDay = pet:w():hourOfDay() --tem q linkar com o horario do jogo

petName = '[park] Pingo' -- provisorio ate ter o codigo que o jogador nomeia o personagem

math.randomseed(os.time())

--[[eventos que acontecem entre 08:00AM to 12:00PM
--]]

if hourOfDay >= 08 or
   hourOfDay <= 12 then
	petPark1 = math.random(1,20)
	if petPark1 == 1 then
	print(petName, "fed the PIGEONS.")
	end
	if petPark1 == 2 then
	print(petName, "ate popcorn and started a friendship with POPCORN GUY.")
	end
	if petPark1 == 3 then
	print(petName, "became friend with HIPPIES.")
	end
	if petPark1 == 4 then
	print(petName, "sniffed the flowers from the park.")
	end
	if petPark1 == 5 then
	print(petName, "became friend from PIGEON WOMAN. Became scared.")
	end
	if petPark1 == 6 then
	print(petName, "appraized the sun.")
	end
	if petPark1 == 7 then
	print(petName, "visited the Church`s square.")
	end
	if petPark1 == 8 then
	print(petName, "became friend of the COPS.")
	end
	if petPark1 == 9 then
	print(petName, "received popcorn from the POPCORN GUY.")
	end
	if petPark1 == 10 then
	print(petName, "mixed coffee and alcohol.")
	end
	if petPark1 == 11 then
	print(petName, "tripped in HIPPIES` art and was yelled.")
	end
	if petPark1 == 12 then
	print(petName, "was followed by a MUSICIAN.")
	end
	if petPark1 == 13 then
	print(petName, "was flirted by PIGEON WOMAN.")
	end
	if petPark1 == 14 then
	print(petName, "farted close to the COPS. Was notified.")
	end
	if petPark1 == 15 then
	print(petName, "accepted more than 10 publicity folders.")
	end
	if petPark1 == 16 then
	print(petName, "asked something in the wish fountain. Got depressed.")
	end
	if petPark1 == 17 then
	print(petName, "ate a rooten icecream.")
	end
	if petPark1 == 18 then
	print(petName, "was attacked by a group of HIPPIES.")
	end
	if petPark1 == 19 then
	print(petName, "was mugged by a MUSICIAN.")
	end
	if petPark1 == 20 then
	print(petName, "barfed the coffee and fainted over PIGEONS`s shit.")
	end
	io.read();
elseif hourOfDay > 12 or
   hourOfDay <= 17 then
	--[[eventos que acontecem entre 12:00PM to 17:00PM
	--]]
	petPark2 = math.random(1,20)
	if petPark2 == 1 then
	print(petName, "kicked a PIGEON. Felt powerfull.")
	end
	if petPark2 == 2 then
	print(petName, "pissed in the flowers of the garden. Releaf.")
	end
	if petPark2 == 3 then
	print(petName, "bought a juice and felt better.")
	end
	if petPark2 == 4 then
	print(petName, "avoided the HOBBOS.")
	end
	if petPark2 == 5 then
	print(petName, "ate old food and felt good.")
	end
	if petPark2 == 6 then
	print(petName, "declined scotch offered by the DRUNKERS.")
	end
	if petPark2 == 7 then
	print(petName, "was flirted by PIGEON WOMAN.")
	end
	if petPark2 == 8 then
	print(petName, "was followed by a hungry HOBBO.")
	end
	if petPark2 == 9 then
	print(petName, "took a shit from a PIGEON on the shoulder.")
	end
	if petPark2 == 10 then
	print(petName, "sweat too much. Some PRETTY TOURISTS didn`t like it.")
	end
	if petPark2 == 11 then
	print(petName, "forgot to remove the pants beforee pee.")
	end
	if petPark2 == 12 then
	print(petName, "asked information to the COPS and was ignored.")
	end
	if petPark2 == 13 then
	print(petName, "followed a HOBBO that stoled his wallet.")
	end
	if petPark2 == 14 then
	print(petName, "discussed the meaning of life with POPCORN GUY. They did not concur.")
	end
	if petPark2 == 15 then
	print(petName, "realized that it went to the square naked.")
	end
	if petPark2 == 16 then
	print(petName, "drank with some DRUNKERS in the square fountain.")
	end
	if petPark2 == 17 then
	print(petName, "was attacked agressivelly by the PIGEONS.")
	end
	if petPark2 == 18 then
	print(petName, "was attacked by PIGEON WOMAN. Returned in shock.")
	end
	if petPark2 == 19 then
	print(petName, "got drunk with POPCORN GUY oils.")
	end
	if petPark2 == 20 then
	print(petName, "drunk and barfed. Was beaten by the DRUNKERS to waste the alcool.")
	end
	io.read();
elseif hourOfDay > 17 or
		hourOfDay <= 22 then
	--[[eventos que acontecem entre 17:01PM to 22:00PM
	--]]
	petPark3 = math.random(1,20)
	if petPark3 == 1 then
	print(petName, "fed the PIGEON WOMAN.")
	end
	if petPark3 == 2 then
	print(petName, "shit under the tree.")
	end
	if petPark3 == 3 then
	print(petName, "admired the moon.")
	end
	if petPark3 == 4 then
	print(petName, "took a shit from the PIGEONS on the shoulder.")
	end
	if petPark3 == 5 then
	print(petName, "took a danger way. Got scared.")
	end
	if petPark3 == 6 then
	print(petName, "was intimidated by the DRUNKERS. Scary.")
	end
	if petPark3 == 7 then
	print(petName, "bought drugs from a seller.")
	end
	if petPark3 == 8 then
	print(petName, "drank alone in Church stairs.")
	end
	if petPark3 == 9 then
	print(petName, "was pissing the TAXI DRIVERS. Was ignored.")
	end
	if petPark3 == 10 then
	print(petName, "accepted a cigarrete from a STRANGER.")
	end
	if petPark3 == 11 then
	print(petName, "was fingered by a STRANGER.")
	end
	if petPark3 == 12 then
	print(petName, "steped in a sleeping HOBBO. Had to flee.")
	end
	if petPark3 == 13 then
	print(petName, "asked for money, received beer.")
	end
	if petPark3 == 14 then
	print(petName, " talked with PIGEON WOMAN. She tried to kiss him.")
	end
	if petPark3 == 15 then
	print(petName, "slept in the square, woke up huggin a STRANGER.")
	end
	if petPark3 == 16 then
	print(petName, "was fingered by a STRANGER, that presented himself and followed", petName, "until home.")
	end
	if petPark3 == 17 then
	print(petName, "was registered by the COPS as a hobbo.")
	end
	if petPark3 == 18 then
	print(petName, "was kidnapped to the forest. Returned in shock.")
	end
	if petPark3 == 19 then
	print(petName, "was mugged by PIGEON WOMAN. Returned without underpants.")
	end
	if petPark3 == 20 then
	print(petName, "drank too much with DRUNKERS.")
	end
	io.read();
end
