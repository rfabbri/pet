-- Script da aula lua#12. Quando integrado com lua, tentar adaptar esse script.
Script.size = Vec2(50, 5) --Vec2 "Size"
Script.backgroundColor = Vec4() -- Color "Background Color"
Script.healthBarColor = Vec4() -- Color "Health Bar Color"

function Script:Start()

end

functon Script:PostRender(context)

	--background
	local color = Vec4 ( 	self.backgroundColor.x/255,
							self.backgroundColor.y/255,
							self.backgroundColor.z/255,
							self.backgroundColor.w/255)
						
	context:SetColor(color)
	context:DrawnRect(0, context:GetHeight() - self.size.y, self.size.x, self.size.y)

	--health bar
	local color = Vec4 ( 	self.healtBarColor.x/255,
							self.healtBarColor.y/255,
							self.healtBarColor.z/255,
							self.healtBarColor.w/255)
						
	context:SetColor(color)
	local healthFactor = self.player.script.health / self.player.script.maxHealth
	context:DrawnRect(0, context:GetHeight() - self.size.y, self.size.x * healthFactor, self.size.y)
end
