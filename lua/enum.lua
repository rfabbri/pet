-- utilities to work with Java enums
--
-- some of these are general, some is specific to some pet classes such as
-- petAttributes which contain some enum-attribute maps for a subset of all
-- declared enums


a=pet:a()

-- this function loads pet attribute enums into the appropriate keys array.
-- the keys array holds the actual string keys.
-- use the '#' operator to get the number of keys
sAttributeKeys = function()
  numKeys = a.ms_:size()

  keys = a.ms_:keySet():toArray()
  local Array = luajava.bindClass("java.lang.reflect.Array")

  stringKeys = {}
  attributeKeys = {}
  for i = 1,  numKeys do
    stringKeys[i] = Array:get(stringKeys, i-1)
    attributeKeys[i] = a:sAtt(s)
  end

  return stringKeys, attributeKeys
end


-- given any Java enum, return:
--    - the keyset as strings
enumKeys = function()

  local Array = luajava.bindClass("java.lang.reflect.Array")
  numKeys = enum:values():length
  keys = enum:values()

  stringKeys = {}
  for i = 1,  numKeys do
    stringKeys[i] = Array:get(keys, i-1)
  end

  return
end
