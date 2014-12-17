
--[[Falta inserir a linha pra direcionar para
petWork1, petWork2, petWork3 de acordo
com o horario do jogador.
PS: como faz pro petTime rastrear o horario do jogador?
PS2: revisar se o goto funciona
PS3: como adicionar o mod de atributos?

petTime = ?????

if petTime >= 08:00:00 or
   petTime <= 12:00:00 then
   goto petWork1
end

if petTime >= 12:01:00 or
   petTime <= 17:00:00 then
   goto petWork2
end

if petTime >= 17:01:00 or
   petTime <= 22:00:00 then
   goto petWork3
end
--]]

petName = 'Pingo' -- provisorio ate ter o codigo que o jogador nomeia o personagem

--[[eventos que acontecem entre 08:00AM to 12:00PM
--]]

petWork1 = math.random(1,20)
if petWork1 == 1 then
print(petName, "did a great job and received a cash bonus.")
end
if petWork1 == 2 then
print(petName, "received na anonymous love letter.")
end
if petWork1 == 3 then
print(petName, "helped the JANITOR and the friendship improved.")
end
if petWork1 == 4 then
print(petName, "found a coffe ticket on the floor.")
end
if petWork1 == 5 then
print(petName, "got a ride home in a trash truck.")
end
if petWork1 == 6 then
print(petName, "received an extra carrot from cooker girl during lunch.")
end
if petWork1 == 7 then
print(petName, "delivery all the tasks in record time and left early.")
end
if petWork1 == 8 then
print(petName, "helped the WIRDO CHUBBY that was locked in bathroom.")
end
if petWork1 == 9 then
print(petName, "received a hand shake from his BOSS and felt energized.")
end
if petWork1 == 10 then
print(petName, "was locked in the bathroom and could not finish the task.")
end
if petWork1 == 11 then
print(petName, "was shitting and the paper was over.")
end
if petWork1 == 12 then
print(petName, "looked into BOSS penis during mictory. Was not cool.")
end
if petWork1 == 13 then
print(petName, "was hit by a car returning home.")
end
if petWork1 == 14 then
print(petName, "had his money stoled from his desk. He wasn`t refunded.")
end
if petWork1 == 15 then
print(petName, "was caught sleeping under the desk. Received a strike.")
end
if petWork1 == 16 then
print(petName, "tried to flirt with DIRECTOR`s SECRETARY and got punched.")
end
if petWork1 == 17 then
print(petName, "couldn`t handle the cafeteria`s food and vomited.")
end
if petWork1 == 18 then
print(petName, "was caught drinking in the bathroom.")
end
if petWork1 == 19 then
print(petName, "dropped the hot coffee in the HIPSTER GIRL.")
end
if petWork1 == 20 then
print(petName, "was locked in the bathroom and didn`t finish the task. Salary deduction.")
end
io.read();

--[[eventos que acontecem entre 12:00PM to 17:00PM
--]]
petWork2 = math.random(1,20)
if petWork2 == 1 then
print(petName, "received cash bonus due to diversity day.")
end
if petWork2 == 2 then
print(petName, "could have lunch with LATINA GIRL and HIPSTER GIRL at same time.")
end
if petWork2 == 3 then
print(petName, "could use copy machine in a proper way.")
end
if petWork2 == 4 then
print(petName, "was invited to a barbecue.")
end
if petWork2 == 5 then
print(petName, "could help BOSS to get coffee. BOSS liked it.")
end
if petWork2 == 6 then
print(petName, "looked under LATINA GIRL skirt and got excited.")
end
if petWork2 == 7 then
print(petName, "tried to smell LATINA GIRL ass. She farted at same time.")
end
if petWork2 == 8 then
print(petName, "smoked JANITOR cigarrete. Got hungry.")
end
if petWork2 == 9 then
print(petName, "clipped his finger with staple. It hurts.")
end
if petWork2 == 10 then
print(petName, "farted in cafeteria. People vomited and was nasty.")
end
if petWork2 == 11 then
print(petName, "offered a beer to the BOSS and received a strike.")
end
if petWork2 == 12 then
print(petName, "tried to hit WIRDO CHUBBY with a paper ball. HIPSTER GIRL was hit.")
end
if petWork2 == 13 then
print(petName, "send a pervert email to DIRECTOR`s SECRETARY. She liked.")
end
if petWork2 == 14 then
print(petName, "was releafing in chair`s leg. HOT LATINA screammed.")
end
if petWork2 == 15 then
print(petName, "lost the bus and arrived late and received a strike.")
end
if petWork2 == 16 then
print(petName, "could not explain why all tasks are delaying.")
end
if petWork2 == 17 then
print(petName, "was punched in the face after try to pick other carrot during lunch.")
end
if petWork2 == 18 then
print(petName, "tried to start a motim and received a strike from HR.")
end
if petWork2 == 19 then
print(petName, "vomited in HIPSTER GIRL glasses. She cried.")
end
if petWork2 == 20 then
print(petName, "left the bathroom without pants. People left running through emergency doors.")
end
io.read();

--[[eventos que acontecem entre 17:01PM to 22:00PM
--]]
petWork3 = math.random(1,20)
if petWork3 == 1 then
print(petName, "did a great job and was promoted.")
end
if petWork3 == 2 then
print(petName, "kissed HIPSTER GIRL in the back of the copy machine.")
end
if petWork3 == 3 then
print(petName, "flirted with HOT LATINA and she liked.")
end
if petWork3 == 4 then
print(petName, "dropped hot coffee on himself. It hurts.")
end
if petWork3 == 5 then
print(petName, "burp in the office. People laugh.")
end
if petWork3 == 6 then
print(petName, "got flu from someone in his job.")
end
if petWork3 == 7 then
print(petName, "got stoled in his job. Lost money.")
end
if petWork3 == 8 then
print(petName, "was punched by JANITOR for shitting on the floor.")
end
if petWork3 == 9 then
print(petName, "tried to kiss HIPSTER GIRL and was not successful. Punch received.")
end
if petWork3 == 10 then
print(petName, "tried to put a pin in the BOSS chair and was caught. Strike received.")
end
if petWork3 == 11 then
print(petName, "left early from the job and had a deduction salary.")
end
if petWork3 == 12 then
print(petName, "arrived drunk in the job and HOT LATINA noticed.")
end
if petWork3 == 13 then
print(petName, "tripped with hot coffee in hands and got hurt.")
end
if petWork3 == 14 then
print(petName, "slept during the job. Woke up with the BOSS.")
end
if petWork3 == 15 then
print(petName, "said nice tits to DIRECTOR`s SECRETARY. She was horrified.")
end
if petWork3 == 16 then
print(petName, "could not hold and shit during a meeting. People cried.")
end
if petWork3 == 17 then
print(petName, "farted in a meeting with his BOSS.")
end
if petWork3 == 18 then
print(petName, "was challenge to staple his finger. He tried and cried.")
end
if petWork3 == 19 then
print(petName, "was caught adding alcool in the coffee machine. Received a strike.")
end
if petWork3 == 20 then
print(petName, "had a fight with WIRDO CHUBBY. Got smashed.")
end
io.read();
