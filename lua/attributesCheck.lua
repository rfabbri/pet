--
-- Assumir
--
-- stringKeys:
--
--
--
-- attributeKeys


-- Exemplo:
--     stringKeys[3] == "Ressaca"

stringKeys, attributeKeys = sAttributeKeys()

if !inicializado then
  acs = {}
  for i = 1, #stringKeys do
    acs[i] = {}
  end

  inicializado = true
end



for i = 1, #stringKeys do
  print("atributo nome: ", stringKeys[i])
  print("atributo: ", attributeKeys[i])
  -- Ideia:
  --
  -- acs["Fome"]["Faminto"] += 1

  acs[stringKeys[i]][attributeKeys[i]:get()] += 1;
end


if acs["Alcool"]["Bebado"] > 0.8 then
  print "pet virou um tiozao"
  
end


--[[ 
Proposta script - Presto

acs1= {}
acs1nutricao = {}
acs1humor = {}
acs1social = {}
acs1higiene = {}
acs1estudo = {}
acs1saude = {}
acs1disciplina = {}
acs1alcool = {}
acs1vida = {}
acs1sexualidade = {}
acs1fe = {}


on 24 hours

	set value acs1["NUTRICAO"]["FAMINTO"] = acs["NUTRICAO"]["FAMINTO"]
	set value acs1["NUTRICAO"]["MUITA_FOME"] = acs["NUTRICAO"]["MUITA_FOME"]
	set value acs1["NUTRICAO"]["FOME"] = acs["NUTRICAO"]["FOME"]
	set value acs1["NUTRICAO"]["SATISFEITO"] = acs["NUTRICAO"]["SATISFEITO"]
	set value acs1["NUTRICAO"]["CHEIO"] = acs["NUTRICAO"]["CHEIO"]
	set value acs1["NUTRICAO"]["LOTADO"] = acs["NUTRICAO"]["LOTADO"]
	
	set value acs1["HUMOR"]["BRAVO"] = acs["HUMOR"]["BRAVO"]
	set value acs1["HUMOR"]["IRRITADO"] = acs["HUMOR"]["IRRITADO"]
	set value acs1["HUMOR"]["ENTEDIADO"] = acs["HUMOR"]["ENTEDIADO"]
	set value acs1["HUMOR"]["ENTRETIDO"] = acs["HUMOR"]["ENTRETIDO"]
	set value acs1["HUMOR"]["ALEGRE"] = acs["HUMOR"]["ALEGRE"]
	set value acs1["HUMOR"]["MUITO_ALEGRE"] = acs["HUMOR"]["MUITO_ALEGRE"]
	
	set value acs1["SOCIAL"]["DEPRESSAO"] = acs["SOCIAL"]["DEPRESSAO"]
	set value acs1["SOCIAL"]["SOZINHO"] = acs["SOCIAL"]["SOZINHO"]
	set value acs1["SOCIAL"]["POUCOS_AMIGOS"] = acs["SOCIAL"]["POUCOS_AMIGOS"]
	set value acs1["SOCIAL"]["POPULAR"] = acs["SOCIAL"]["POPULAR"]
	set value acs1["SOCIAL"]["SUPERSTAR"] = acs["SOCIAL"]["SUPERSTAR"]
	
	set value acs1["HIGIENE"]["IMUNDO"] = acs["HIGIENE"]["IMUNDO"]
	set value acs1["HIGIENE"]["MUITO_SUJO"] = acs["HIGIENE"]["MUITO_SUJO"]
	set value acs1["HIGIENE"]["SUJO"] = acs["HIGIENE"]["SUJO"]
	set value acs1["HIGIENE"]["LIMPO"] = acs["HIGIENE"]["LIMPO"]
	set value acs1["HIGIENE"]["MUITO_LINDO"] = acs["HIGIENE"]["MUITO_LINDO"]

	set value acs1["ESTUDO"]["EXPULSO"] = acs["ESTUDO"]["EXPULSO"]
	set value acs1["ESTUDO"]["REPROVADO"] = acs["ESTUDO"]["REPROVADO"]
	set value acs1["ESTUDO"]["RECUPERACAO"] = acs["ESTUDO"]["RECUPERACAO"]
	set value acs1["ESTUDO"]["MEDIANO"] = acs["ESTUDO"]["MEDIANO"]
	set value acs1["ESTUDO"]["BOM_ALUNO"] = acs["ESTUDO"]["BOM_ALUNO"]
	set value acs1["ESTUDO"]["MELHOR_DA_SALA"] = acs["ESTUDO"]["MELHOR_DA_SALA"]
	
	set value acs1["SAUDE"]["MORTO_DOENTE"] = acs["SAUDE"]["MORTO_DOENTE"]
	set value acs1["SAUDE"]["DOENTE_TERMINAL"] = acs["SAUDE"]["DOENTE_TERMINAL"]
	set value acs1["SAUDE"]["DOENTE"] = acs["SAUDE"]["DOENTE"]
	set value acs1["SAUDE"]["FRAGIL"] = acs["SAUDE"]["FRAGIL"]
	set value acs1["SAUDE"]["ESTAVEL"] = acs["SAUDE"]["ESTAVEL"]
	set value acs1["SAUDE"]["SAUDAVEL"] = acs["SAUDE"]["SAUDAVEL"]
	set value acs1["SAUDE"]["ATLETA"] = acs["SAUDE"]["ATLETA"]

	set value acs1["DISCIPLINA"]["PRESO"] = acs["DISCIPLINA"]["PRESO"]
	set value acs1["DISCIPLINA"]["CRIMINOSO"] = acs["DISCIPLINA"]["CRIMINOSO"]
	set value acs1["DISCIPLINA"]["REBELDE"] = acs["DISCIPLINA"]["REBELDE"]
	set value acs1["DISCIPLINA"]["TEIMOSO"] = acs["DISCIPLINA"]["TEIMOSO"]
	set value acs1["DISCIPLINA"]["OBEDIENTE"] = acs["DISCIPLINA"]["OBEDIENTE"]
	set value acs1["DISCIPLINA"]["RESPONSAVEL"] = acs["DISCIPLINA"]["RESPONSAVEL"]
	set value acs1["DISCIPLINA"]["MAQUINA_DISCIPLINAR"] = acs["DISCIPLINA"]["MAQUINA_DISCIPLINAR"]
	
	set value acs1["ALCOOL"]["SOBRIO"] = acs["ALCOOL"]["SOBRIO"]
	set value acs1["ALCOOL"]["TONTO"] = acs["ALCOOL"]["TONTO"]
	set value acs1["ALCOOL"]["BEBADO"] = acs["ALCOOL"]["BEBADO"]
	set value acs1["ALCOOL"]["MUITO_BEBADO"] = acs["ALCOOL"]["MUITO_BEBADO"]
	set value acs1["ALCOOL"]["COMA_ALCOOLICO"] = acs["ALCOOL"]["COMA_ALCOOLICO"]
	
	set value acs1["VIDA"]["COMA"] = acs["VIDA"]["COMA"]
	set value acs1["VIDA"]["DILACERADO"] = acs["VIDA"]["DILACERADO"]
	set value acs1["VIDA"]["MUITO_MACHUCADO"] = acs["VIDA"]["MUITO_MACHUCADO"]
	set value acs1["VIDA"]["MACHUCADO"] = acs["VIDA"]["MACHUCADO"]
	set value acs1["VIDA"]["FERIDO"] = acs["VIDA"]["FERIDO"]
	
	set value acs1["SEXUALIDADE"]["ASSEXUADO"] = acs["SEXUALIDADE"]["ASSEXUADO"]
	set value acs1["SEXUALIDADE"]["VIRGEM"] = acs["SEXUALIDADE"]["VIRGEM"]
	set value acs1["SEXUALIDADE"]["INEXPERIENTE"] = acs["SEXUALIDADE"]["INEXPERIENTE"]
	set value acs1["SEXUALIDADE"]["APRECIADOR_DO_SEXO"] = acs["SEXUALIDADE"]["APRECIADOR_DO_SEXO"]
	set value acs1["SEXUALIDADE"]["SEXO_SELVAGEM"] = acs["SEXUALIDADE"]["SEXO_SELVAGEM"]
	set value acs1["SEXUALIDADE"]["MICHAEL_DOUGLAS"] = acs["SEXUALIDADE"]["MICHAEL_DOUGLAS"]
	set value acs1["SEXUALIDADE"]["NINFOMANIACO"] = acs["SEXUALIDADE"]["NINFOMANIACO"]
	
	set value acs1["FE"]["ANTI_CRISTO"] = acs["FE"]["ANTI_CRISTO"]
	set value acs1["FE"]["FIEL_FERVOROSO"] = acs["FE"]["FIEL_FERVOROSO"]
	
-- Porcentagem dos status por atributo
	
	set value acs1nutricao["FAMINTO"] = 	acs1["NUTRICAO"]["FAMINTO"] + 
											acs1["NUTRICAO"]["MUITA_FOME"] + 
											acs1["NUTRICAO"]["FOME"] + 
											acs1["NUTRICAO"]["SATISFEITO"] +
											acs1["NUTRICAO"]["CHEIO"] + 
											acs1["NUTRICAO"]["LOTADO"] /
											acs1["NUTRICAO"]["FAMINTO"]
											
	set value acs1nutricao["MUITA_FOME"] = 	acs1["NUTRICAO"]["FAMINTO"] + 
											acs1["NUTRICAO"]["MUITA_FOME"] + 
											acs1["NUTRICAO"]["FOME"] + 
											acs1["NUTRICAO"]["SATISFEITO"] +
											acs1["NUTRICAO"]["CHEIO"] + 
											acs1["NUTRICAO"]["LOTADO"] /
											acs1["NUTRICAO"]["MUITA_FOME"]
														
	set value acs1nutricao["FOME"] = 		acs1["NUTRICAO"]["FAMINTO"] + 
											acs1["NUTRICAO"]["MUITA_FOME"] + 
											acs1["NUTRICAO"]["FOME"] + 
											acs1["NUTRICAO"]["SATISFEITO"] +
											acs1["NUTRICAO"]["CHEIO"] + 
											acs1["NUTRICAO"]["LOTADO"] /
											acs1["NUTRICAO"]["FOME"]										
											
	set value acs1nutricao["SATISFEITO"] = 	acs1["NUTRICAO"]["FAMINTO"] + 
											acs1["NUTRICAO"]["MUITA_FOME"] + 
											acs1["NUTRICAO"]["FOME"] + 
											acs1["NUTRICAO"]["SATISFEITO"] +
											acs1["NUTRICAO"]["CHEIO"] + 
											acs1["NUTRICAO"]["LOTADO"] /
											acs1["NUTRICAO"]["SATISFEITO"]

	set value acs1nutricao["CHEIO"] = 	acs1["NUTRICAO"]["FAMINTO"] + 
											acs1["NUTRICAO"]["MUITA_FOME"] + 
											acs1["NUTRICAO"]["FOME"] + 
											acs1["NUTRICAO"]["SATISFEITO"] +
											acs1["NUTRICAO"]["CHEIO"] + 
											acs1["NUTRICAO"]["LOTADO"] /
											acs1["NUTRICAO"]["CHEIO"]

	set value acs1nutricao["LOTADO"] = 		acs1["NUTRICAO"]["FAMINTO"] + 
											acs1["NUTRICAO"]["MUITA_FOME"] + 
											acs1["NUTRICAO"]["FOME"] + 
											acs1["NUTRICAO"]["SATISFEITO"] +
											acs1["NUTRICAO"]["CHEIO"] + 
											acs1["NUTRICAO"]["LOTADO"] /
											acs1["NUTRICAO"]["LOTADO"]
	
	set value acs1humor["BRAVO"] = 			acs1["HUMOR"]["BRAVO"] +
											acs1["HUMOR"]["IRRITADO"] +
											acs1["HUMOR"]["ENTEDIADO"] +
											acs1["HUMOR"]["ENTRETIDO"] +
											acs1["HUMOR"]["ALEGRE"] +
											acs1["HUMOR"]["MUITO_ALEGRE"] / 
											acs1["HUMOR"]["BRAVO"]

	set value acs1humor["IRRITADO"] =		acs1["HUMOR"]["BRAVO"] +
											acs1["HUMOR"]["IRRITADO"] +
											acs1["HUMOR"]["ENTEDIADO"] +
											acs1["HUMOR"]["ENTRETIDO"] +
											acs1["HUMOR"]["ALEGRE"] +
											acs1["HUMOR"]["MUITO_ALEGRE"] / 
											acs1["HUMOR"]["IRRITADO"]
								
	set value acs1humor["ENTEDIADO"] = 		acs1["HUMOR"]["BRAVO"] +
											acs1["HUMOR"]["IRRITADO"] +
											acs1["HUMOR"]["ENTEDIADO"] +
											acs1["HUMOR"]["ENTRETIDO"] +
											acs1["HUMOR"]["ALEGRE"] +
											acs1["HUMOR"]["MUITO_ALEGRE"] / 
											acs1["HUMOR"]["ENTEDIADO"]

	set value acs1humor["ENTRETIDO"] = 		acs1["HUMOR"]["BRAVO"] +
											acs1["HUMOR"]["IRRITADO"] +
											acs1["HUMOR"]["ENTENDIADO"] +
											acs1["HUMOR"]["ENTRETIDO"] +
											acs1["HUMOR"]["ALEGRE"] +
											acs1["HUMOR"]["MUITO_ALEGRE"] / 
											acs1["HUMOR"]["ENTRETIDO"]

	set value acs1humor["ALEGRE"] = 		acs1["HUMOR"]["BRAVO"] +
											acs1["HUMOR"]["IRRITADO"] +
											acs1["HUMOR"]["ENTENDIADO"] +
											acs1["HUMOR"]["ENTRETIDO"] +
											acs1["HUMOR"]["ALEGRE"] +
											acs1["HUMOR"]["MUITO_ALEGRE"] / 
											acs1["HUMOR"]["ALEGRE"]

	set value acs1humor["MUITO_ALEGRE"] =	acs1["HUMOR"]["BRAVO"] +
											acs1["HUMOR"]["IRRITADO"] +
											acs1["HUMOR"]["ENTENDIADO"] +
											acs1["HUMOR"]["ENTRETIDO"] +
											acs1["HUMOR"]["ALEGRE"] +
											acs1["HUMOR"]["MUITO_ALEGRE"] / 
											acs1["HUMOR"]["MUITO_ALEGRE"]

	set value acs1social["DEPRESSAO"] = 	acs1["SOCIAL"]["DEPRESSAO"] +
											acs1["SOCIAL"]["SOZINHO"] +
											acs1["SOCIAL"]["POUCOS_AMIGOS"] +
											acs1["SOCIAL"]["POPULAR"] + 
											acs1["SOCIAL"]["SUPERSTAR"] /
											acs1["SOCIAL"]["DEPRESSAO"]

	set value acs1social["SOZINHO"] = 		acs1["SOCIAL"]["DEPRESSAO"] +
											acs1["SOCIAL"]["SOZINHO"] +
											acs1["SOCIAL"]["POUCOS_AMIGOS"] +
											acs1["SOCIAL"]["POPULAR"] + 
											acs1["SOCIAL"]["SUPERSTAR"] /
											acs1["SOCIAL"]["SOZINHO"]

	set value acs1social["POUCOS_AMIGOS"] = acs1["SOCIAL"]["DEPRESSAO"] +
											acs1["SOCIAL"]["SOZINHO"] +
											acs1["SOCIAL"]["POUCOS_AMIGOS"] +
											acs1["SOCIAL"]["POPULAR"] + 
											acs1["SOCIAL"]["SUPERSTAR"] /
											acs1["SOCIAL"]["POUCOS_AMIGOS"]
											
	set value acs1social["POPULAR"] = 		acs1["SOCIAL"]["DEPRESSAO"] +
											acs1["SOCIAL"]["SOZINHO"] +
											acs1["SOCIAL"]["POUCOS_AMIGOS"] +
											acs1["SOCIAL"]["POPULAR"] + 
											acs1["SOCIAL"]["SUPERSTAR"] /
											acs1["SOCIAL"]["POPULAR"]

	set value acs1social["SUPERSTAR"] =		acs1["SOCIAL"]["DEPRESSAO"] +
											acs1["SOCIAL"]["SOZINHO"] +
											acs1["SOCIAL"]["POUCOS_AMIGOS"] +
											acs1["SOCIAL"]["POPULAR"] + 
											acs1["SOCIAL"]["SUPERSTAR"] /
											acs1["SOCIAL"]["SUPERSTAR"]

	set value acs1higiene["IMUNDO"] = 		acs1["HIGIENE"]["IMUNDO"] +
											acs1["HIGIENE"]["MUITO_SUJO"] +
											acs1["HIGIENE"]["SUJO"] +
											acs1["HIGIENE"]["LIMPO"] + 
											acs1["HIGIENE"]["MUITO_LINDO"] /
											acs1["HIGIENE"]["IMUNDO"] 
										
	set value acs1higiene["MUITO_SUJO"] =	acs1["HIGIENE"]["IMUNDO"] +
											acs1["HIGIENE"]["MUITO_SUJO"] +
											acs1["HIGIENE"]["SUJO"] +
											acs1["HIGIENE"]["LIMPO"] + 
											acs1["HIGIENE"]["MUITO_LINDO"] /
											acs1["HIGIENE"]["MUITO_SUJO"] 

	set value acs1higiene["SUJO"] =			acs1["HIGIENE"]["IMUNDO"] +
											acs1["HIGIENE"]["MUITO_SUJO"] +
											acs1["HIGIENE"]["SUJO"] +
											acs1["HIGIENE"]["LIMPO"] + 
											acs1["HIGIENE"]["MUITO_LINDO"] /
											acs1["HIGIENE"]["SUJO"]
										
	set value acs1higiene["LIMPO"] =		acs1["HIGIENE"]["IMUNDO"] +
											acs1["HIGIENE"]["MUITO_SUJO"] +
											acs1["HIGIENE"]["SUJO"] +
											acs1["HIGIENE"]["LIMPO"] + 
											acs1["HIGIENE"]["MUITO_LINDO"] /
											acs1["HIGIENE"]["LIMPO"]
											
	set value acs1higiene["MUITO_LIMPO"] =	acs1["HIGIENE"]["IMUNDO"] +
											acs1["HIGIENE"]["MUITO_SUJO"] +
											acs1["HIGIENE"]["SUJO"] +
											acs1["HIGIENE"]["LIMPO"] + 
											acs1["HIGIENE"]["MUITO_LINDO"] /
											acs1["HIGIENE"]["MUITO_LIMPO"]
											
	set value acs1estudo["EXPULSO"] = 		acs1["ESTUDO"]["EXPULSO"] +
											acs1["ESTUDO"]["REPROVADO"] +
											acs1["ESTUDO"]["RECUPERACAO"] +
											acs1["ESTUDO"]["MEDIANO"] +
											acs1["ESTUDO"]["BOM_ALUNO"] +
											acs1["ESTUDO"]["MELHOR_DA_SALA"] /
											acs1["ESTUDO"]["EXPULSO"]
	
	set value acs1estudo["REPROVADO"] = 	acs1["ESTUDO"]["EXPULSO"] +
											acs1["ESTUDO"]["REPROVADO"] +
											acs1["ESTUDO"]["RECUPERACAO"] +
											acs1["ESTUDO"]["MEDIANO"] +
											acs1["ESTUDO"]["BOM_ALUNO"] +
											acs1["ESTUDO"]["MELHOR_DA_SALA"] /
											acs1["ESTUDO"]["REPROVADO"]

	set value acs1estudo["RECUPERACAO"] = 	acs1["ESTUDO"]["EXPULSO"] +
											acs1["ESTUDO"]["REPROVADO"] +
											acs1["ESTUDO"]["RECUPERACAO"] +
											acs1["ESTUDO"]["MEDIANO"] +
											acs1["ESTUDO"]["BOM_ALUNO"] +
											acs1["ESTUDO"]["MELHOR_DA_SALA"] /
											acs1["ESTUDO"]["RECUPERACAO"]
											
	set value acs1estudo["MEDIANO"] = 		acs1["ESTUDO"]["EXPULSO"] +
											acs1["ESTUDO"]["REPROVADO"] +
											acs1["ESTUDO"]["RECUPERACAO"] +
											acs1["ESTUDO"]["MEDIANO"] +
											acs1["ESTUDO"]["BOM_ALUNO"] +
											acs1["ESTUDO"]["MELHOR_DA_SALA"] /
											acs1["ESTUDO"]["MEDIANO"]
											
	set value acs1estudo["BOM_ALUNO"] =		acs1["ESTUDO"]["EXPULSO"] +
											acs1["ESTUDO"]["REPROVADO"] +
											acs1["ESTUDO"]["RECUPERACAO"] +
											acs1["ESTUDO"]["MEDIANO"] +
											acs1["ESTUDO"]["BOM_ALUNO"] +
											acs1["ESTUDO"]["MELHOR_DA_SALA"] /
											acs1["ESTUDO"]["BOM_ALUNO"]
	
	set value acs1estudo["MELHOR_DA_SALA"] =acs1["ESTUDO"]["EXPULSO"] +
											acs1["ESTUDO"]["REPROVADO"] +
											acs1["ESTUDO"]["RECUPERACAO"] +
											acs1["ESTUDO"]["MEDIANO"] +
											acs1["ESTUDO"]["BOM_ALUNO"] +
											acs1["ESTUDO"]["MELHOR_DA_SALA"] /
											acs1["ESTUDO"]["MELHOR_DA_SALA"]
											
	set value acs1saude["MORTO_DOENTE"] = 	acs1["SAUDE"]["MORTO_DOENTE"] +
											acs1["SAUDE"]["DOENTE_TERMINAL"] +
											acs1["SAUDE"]["DOENTE"] +
											acs1["SAUDE"]["FRAGIL"] +
											acs1["SAUDE"]["ESTAVEL"] +
											acs1["SAUDE"]["SAUDAVEL"] +
											acs1["SAUDE"]["ATLETA"] / 
											acs1["SAUDE"]["MORTO_DOENTE"]

	set value acs1saude["DOENTE_TERMINAL"] =acs1["SAUDE"]["MORTO_DOENTE"] +
											acs1["SAUDE"]["DOENTE_TERMINAL"] +
											acs1["SAUDE"]["DOENTE"] +
											acs1["SAUDE"]["FRAGIL"] +
											acs1["SAUDE"]["ESTAVEL"] +
											acs1["SAUDE"]["SAUDAVEL"] +
											acs1["SAUDE"]["ATLETA"] / 
											acs1["SAUDE"]["DOENTE_TERMINAL"]

	set value acs1saude["DOENTE"] = 		acs1["SAUDE"]["MORTO_DOENTE"] +
											acs1["SAUDE"]["DOENTE_TERMINAL"] +
											acs1["SAUDE"]["DOENTE"] +
											acs1["SAUDE"]["FRAGIL"] +
											acs1["SAUDE"]["ESTAVEL"] +
											acs1["SAUDE"]["SAUDAVEL"] +
											acs1["SAUDE"]["ATLETA"] / 
											acs1["SAUDE"]["DOENTE"]

	set value acs1saude["FRAGIL"] = 		acs1["SAUDE"]["MORTO_DOENTE"] +
											acs1["SAUDE"]["DOENTE_TERMINAL"] +
											acs1["SAUDE"]["DOENTE"] +
											acs1["SAUDE"]["FRAGIL"] +
											acs1["SAUDE"]["ESTAVEL"] +
											acs1["SAUDE"]["SAUDAVEL"] +
											acs1["SAUDE"]["ATLETA"] / 
											acs1["SAUDE"]["FRAGIL"]

	set value acs1saude["ESTAVEL"] = 		acs1["SAUDE"]["MORTO_DOENTE"] +
											acs1["SAUDE"]["DOENTE_TERMINAL"] +
											acs1["SAUDE"]["DOENTE"] +
											acs1["SAUDE"]["FRAGIL"] +
											acs1["SAUDE"]["ESTAVEL"] +
											acs1["SAUDE"]["SAUDAVEL"] +
											acs1["SAUDE"]["ATLETA"] / 
											acs1["SAUDE"]["ESTAVEL"]

	set value acs1saude["SAUDAVEL"] = 		acs1["SAUDE"]["MORTO_DOENTE"] +
											acs1["SAUDE"]["DOENTE_TERMINAL"] +
											acs1["SAUDE"]["DOENTE"] +
											acs1["SAUDE"]["FRAGIL"] +
											acs1["SAUDE"]["ESTAVEL"] +
											acs1["SAUDE"]["SAUDAVEL"] +
											acs1["SAUDE"]["ATLETA"] / 
											acs1["SAUDE"]["SAUDAVEL"]

	set value acs1saude["ATLETA"] = 		acs1["SAUDE"]["MORTO_DOENTE"] +
											acs1["SAUDE"]["DOENTE_TERMINAL"] +
											acs1["SAUDE"]["DOENTE"] +
											acs1["SAUDE"]["FRAGIL"] +
											acs1["SAUDE"]["ESTAVEL"] +
											acs1["SAUDE"]["SAUDAVEL"] +
											acs1["SAUDE"]["ATLETA"] / 
											acs1["SAUDE"]["ATLETA"]

	set value acs1disciplina["PRESO"] = 	acs1["DISCIPLINA"]["PRESO"] +
											acs1["DISCIPLINA"]["CRIMINOSO"] +
											acs1["DISCIPLINA"]["REBELDE"] +
											acs1["DISCIPLINA"]["TEIMOSO"] +
											acs1["DISCIPLINA"]["OBEDIENTE"] +
											acs1["DISCIPLINA"]["RESPONSAVEL"] +
											acs1["DISCIPLINA"]["MAQUINA_DISCIPLINAR"] /
											acs1["DISCIPLINA"]["PRESO"]

	set value acs1disciplina["CRIMINOSO"] = acs1["DISCIPLINA"]["PRESO"] +
											acs1["DISCIPLINA"]["CRIMINOSO"] +
											acs1["DISCIPLINA"]["REBELDE"] +
											acs1["DISCIPLINA"]["TEIMOSO"] +
											acs1["DISCIPLINA"]["OBEDIENTE"] +
											acs1["DISCIPLINA"]["RESPONSAVEL"] +
											acs1["DISCIPLINA"]["MAQUINA_DISCIPLINAR"] /
											acs1["DISCIPLINA"]["CRIMINOSO"]

	set value acs1disciplina["REBELDE"] = 	acs1["DISCIPLINA"]["PRESO"] +
											acs1["DISCIPLINA"]["CRIMINOSO"] +
											acs1["DISCIPLINA"]["REBELDE"] +
											acs1["DISCIPLINA"]["TEIMOSO"] +
											acs1["DISCIPLINA"]["OBEDIENTE"] +
											acs1["DISCIPLINA"]["RESPONSAVEL"] +
											acs1["DISCIPLINA"]["MAQUINA_DISCIPLINAR"] /
											acs1["DISCIPLINA"]["REBELDE"]

	set value acs1disciplina["TEIMOSO"] = 	acs1["DISCIPLINA"]["PRESO"] +
											acs1["DISCIPLINA"]["CRIMINOSO"] +
											acs1["DISCIPLINA"]["REBELDE"] +
											acs1["DISCIPLINA"]["TEIMOSO"] +
											acs1["DISCIPLINA"]["OBEDIENTE"] +
											acs1["DISCIPLINA"]["RESPONSAVEL"] +
											acs1["DISCIPLINA"]["MAQUINA_DISCIPLINAR"] /
											acs1["DISCIPLINA"]["TEIMOSO"]
											
	set value acs1disciplina["OBEDIENTE"] = acs1["DISCIPLINA"]["PRESO"] +
											acs1["DISCIPLINA"]["CRIMINOSO"] +
											acs1["DISCIPLINA"]["REBELDE"] +
											acs1["DISCIPLINA"]["TEIMOSO"] +
											acs1["DISCIPLINA"]["OBEDIENTE"] +
											acs1["DISCIPLINA"]["RESPONSAVEL"] +
											acs1["DISCIPLINA"]["MAQUINA_DISCIPLINAR"] /
											acs1["DISCIPLINA"]["OBEDIENTE"]
											
	set value acs1disciplina["RESPONSAVEL"] = 	acs1["DISCIPLINA"]["PRESO"] +
												acs1["DISCIPLINA"]["CRIMINOSO"] +
												acs1["DISCIPLINA"]["REBELDE"] +
												acs1["DISCIPLINA"]["TEIMOSO"] +
												acs1["DISCIPLINA"]["OBEDIENTE"] +
												acs1["DISCIPLINA"]["RESPONSAVEL"] +
												acs1["DISCIPLINA"]["MAQUINA_DISCIPLINAR"] /
												acs1["DISCIPLINA"]["RESPONSAVEL"]									
											
	set value acs1disciplina["MAQUINA_DISCIPLINAR"] = 	acs1["DISCIPLINA"]["PRESO"] +
														acs1["DISCIPLINA"]["CRIMINOSO"] +
														acs1["DISCIPLINA"]["REBELDE"] +
														acs1["DISCIPLINA"]["TEIMOSO"] +
														acs1["DISCIPLINA"]["OBEDIENTE"] +
														acs1["DISCIPLINA"]["RESPONSAVEL"] +
														acs1["DISCIPLINA"]["MAQUINA_DISCIPLINAR"] /
														acs1["DISCIPLINA"]["MAQUINA_DISCIPLINAR"]											
											
	set value acs1alcool["SOBRIO"] = 			acs1["ALCOOL"]["SOBRIO"] +
												acs1["ALCOOL"]["TONTO"] +
												acs1["ALCOOL"]["BEBADO"] +
												acs1["ALCOOL"]["MUITO_BEBADO"] +
												acs1["ALCOOL"]["COMA_ALCOOLICO"] /
												acs1["ALCOOL"]["SOBRIO"]
	
	set value acs1alcool["TONTO"] = 			acs1["ALCOOL"]["SOBRIO"] +
												acs1["ALCOOL"]["TONTO"] +
												acs1["ALCOOL"]["BEBADO"] +
												acs1["ALCOOL"]["MUITO_BEBADO"] +
												acs1["ALCOOL"]["COMA_ALCOOLICO"] /
												acs1["ALCOOL"]["TONTO"]
	
	set value acs1alcool["BEBADO"] = 			acs1["ALCOOL"]["SOBRIO"] +
												acs1["ALCOOL"]["TONTO"] +
												acs1["ALCOOL"]["BEBADO"] +
												acs1["ALCOOL"]["MUITO_BEBADO"] +
												acs1["ALCOOL"]["COMA_ALCOOLICO"] /
												acs1["ALCOOL"]["BEBADO"]
	
	set value acs1alcool["MUITO_BEBADO"] = 		acs1["ALCOOL"]["SOBRIO"] +
												acs1["ALCOOL"]["TONTO"] +
												acs1["ALCOOL"]["BEBADO"] +
												acs1["ALCOOL"]["MUITO_BEBADO"] +
												acs1["ALCOOL"]["COMA_ALCOOLICO"] /
												acs1["ALCOOL"]["MUITO_BEBADO"]
	
	set value acs1alcool["COMA_ALCOOLICO"] = 	acs1["ALCOOL"]["SOBRIO"] +
												acs1["ALCOOL"]["TONTO"] +
												acs1["ALCOOL"]["BEBADO"] +
												acs1["ALCOOL"]["MUITO_BEBADO"] +
												acs1["ALCOOL"]["COMA_ALCOOLICO"] /
												acs1["ALCOOL"]["COMA_ALCOOLICO"]
	

	set value acs1vida["COMA"] = 				acs1["VIDA"]["COMA"] +
												acs1["VIDA"]["DILACERADO"] +
												acs1["VIDA"]["MUITO_MACHUCADO"] +
												acs1["VIDA"]["MACHUCADO"] +
												acs1["VIDA"]["FERIDO"] /
												acs1["VIDA"]["COMA"]

	set value acs1vida["DILACERADO"] = 			acs1["VIDA"]["COMA"] +
												acs1["VIDA"]["DILACERADO"] +
												acs1["VIDA"]["MUITO_MACHUCADO"] +
												acs1["VIDA"]["MACHUCADO"] +
												acs1["VIDA"]["FERIDO"] /
												acs1["VIDA"]["DILACERADO"]
	
	set value acs1vida["MUITO_MACHUCADO"] = 	acs1["VIDA"]["COMA"] +
												acs1["VIDA"]["DILACERADO"] +
												acs1["VIDA"]["MUITO_MACHUCADO"] +
												acs1["VIDA"]["MACHUCADO"] +
												acs1["VIDA"]["FERIDO"] /
												acs1["VIDA"]["MUITO_MACHUCADO"]
	
	set value acs1vida["MACHUCADO"] = 			acs1["VIDA"]["COMA"] +
												acs1["VIDA"]["DILACERADO"] +
												acs1["VIDA"]["MUITO_MACHUCADO"] +
												acs1["VIDA"]["MACHUCADO"] +
												acs1["VIDA"]["FERIDO"] /
												acs1["VIDA"]["MACHUCADO"]
	
	set value acs1vida["MACHUCADO"] = 			acs1["VIDA"]["COMA"] +
												acs1["VIDA"]["DILACERADO"] +
												acs1["VIDA"]["MUITO_MACHUCADO"] +
												acs1["VIDA"]["MACHUCADO"] +
												acs1["VIDA"]["FERIDO"] /
												acs1["VIDA"]["MACHUCADO"]
	
	set value acs1vida["FERIDO"] = 				acs1["VIDA"]["COMA"] +
												acs1["VIDA"]["DILACERADO"] +
												acs1["VIDA"]["MUITO_MACHUCADO"] +
												acs1["VIDA"]["MACHUCADO"] +
												acs1["VIDA"]["FERIDO"] /
												acs1["VIDA"]["FERIDO"]

	set value acs1sexualidade["ASSEXUADO"] = 	acs1["SEXUALIDADE"]["ASSEXUADO"] +
												acs1["SEXUALIDADE"]["VIRGEM"] +
												acs1["SEXUALIDADE"]["INEXPERIENTE"] +
												acs1["SEXUALIDADE"]["APRECIADOR_DO_SEXO"] +
												acs1["SEXUALIDADE"]["SEXO_SELVAGEM"] +
												acs1["SEXUALIDADE"]["MICHAEL_DOUGLAS"] +
												acs1["SEXUALIDADE"]["NINFOMANIACO"] /
												acs1["SEXUALIDADE"]["ASSEXUADO"]
	
	set value acs1sexualidade["VIRGEM"] = 		acs1["SEXUALIDADE"]["ASSEXUADO"] +
												acs1["SEXUALIDADE"]["VIRGEM"] +
												acs1["SEXUALIDADE"]["INEXPERIENTE"] +
												acs1["SEXUALIDADE"]["APRECIADOR_DO_SEXO"] +
												acs1["SEXUALIDADE"]["SEXO_SELVAGEM"] +
												acs1["SEXUALIDADE"]["MICHAEL_DOUGLAS"] +
												acs1["SEXUALIDADE"]["NINFOMANIACO"] /
												acs1["SEXUALIDADE"]["VIRGEM"]

	set value acs1sexualidade["INEXPERIENTE"] = acs1["SEXUALIDADE"]["ASSEXUADO"] +
												acs1["SEXUALIDADE"]["VIRGEM"] +
												acs1["SEXUALIDADE"]["INEXPERIENTE"] +
												acs1["SEXUALIDADE"]["APRECIADOR_DO_SEXO"] +
												acs1["SEXUALIDADE"]["SEXO_SELVAGEM"] +
												acs1["SEXUALIDADE"]["MICHAEL_DOUGLAS"] +
												acs1["SEXUALIDADE"]["NINFOMANIACO"] /
												acs1["SEXUALIDADE"]["INEXPERIENTE"]

	set value acs1sexualidade["APRECIADOR_DO_SEXO"] = 	acs1["SEXUALIDADE"]["ASSEXUADO"] +
														acs1["SEXUALIDADE"]["VIRGEM"] +
														acs1["SEXUALIDADE"]["INEXPERIENTE"] +
														acs1["SEXUALIDADE"]["APRECIADOR_DO_SEXO"] +
														acs1["SEXUALIDADE"]["SEXO_SELVAGEM"] +
														acs1["SEXUALIDADE"]["MICHAEL_DOUGLAS"] +
														acs1["SEXUALIDADE"]["NINFOMANIACO"] /
														acs1["SEXUALIDADE"]["APRECIADOR_DO_SEXO"]

	set value acs1sexualidade["SEXO_SELVAGEM"] =acs1["SEXUALIDADE"]["ASSEXUADO"] +
												acs1["SEXUALIDADE"]["VIRGEM"] +
												acs1["SEXUALIDADE"]["INEXPERIENTE"] +
												acs1["SEXUALIDADE"]["APRECIADOR_DO_SEXO"] +
												acs1["SEXUALIDADE"]["SEXO_SELVAGEM"] +
												acs1["SEXUALIDADE"]["MICHAEL_DOUGLAS"] +
												acs1["SEXUALIDADE"]["NINFOMANIACO"] /
												acs1["SEXUALIDADE"]["SEXO_SELVAGEM"]

	set value acs1sexualidade["MICHAEL_DOUGLAS"] = 	acs1["SEXUALIDADE"]["ASSEXUADO"] +
													acs1["SEXUALIDADE"]["VIRGEM"] +
													acs1["SEXUALIDADE"]["INEXPERIENTE"] +
													acs1["SEXUALIDADE"]["APRECIADOR_DO_SEXO"] +
													acs1["SEXUALIDADE"]["SEXO_SELVAGEM"] +
													acs1["SEXUALIDADE"]["MICHAEL_DOUGLAS"] +
													acs1["SEXUALIDADE"]["NINFOMANIACO"] /
													acs1["SEXUALIDADE"]["MICHAEL_DOUGLAS"]

	set value acs1sexualidade["NINFOMANIACO"] = acs1["SEXUALIDADE"]["ASSEXUADO"] +
												acs1["SEXUALIDADE"]["VIRGEM"] +
												acs1["SEXUALIDADE"]["INEXPERIENTE"] +
												acs1["SEXUALIDADE"]["APRECIADOR_DO_SEXO"] +
												acs1["SEXUALIDADE"]["SEXO_SELVAGEM"] +
												acs1["SEXUALIDADE"]["MICHAEL_DOUGLAS"] +
												acs1["SEXUALIDADE"]["NINFOMANIACO"] /
												acs1["SEXUALIDADE"]["NINFOMANIACO"]

	set value acs1fe["ANTI_CRISTO"] = 			acs1["FE"]["ANTI_CRISTO"] +
												acs1["FE"]["FIEL_FERVOROSO"] /
												acs1["FE"]["ANTI_CRISTO"]

	set value acs1fe["FIEL_FERVOROSO"] =		acs1["FE"]["ANTI_CRISTO"] +
												acs1["FE"]["FIEL_FERVOROSO"] /
												acs1["FE"]["FIEL_FERVOROSO"]

	
  print acs1nutricao
  print acs1humor
  print acs1social
  print acs1higiene
  print acs1estudo
  print acs1saude
  print acs1alcool
  print acs1disciplina
  print acs1vida
  print acs1sexualidade
  print acs1fe
 
--]]
