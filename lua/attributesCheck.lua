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
