extends AnimatedSprite

const SLOW_DOWN_RATE = .5
const DRINK_BOOST = .05

class State:
	var rate
	var buffer
	var anim
	
	func _init(rate, buffer, animation):
		self.rate = rate
		self.buffer = buffer
		self.anim = animation

# drink speed states
var HEAVY = State.new(1, .8, "heavy")
var MEDIUM = State.new(.5, .3, "med")
var LIGHT = State.new(.25, .1, "light")
var SIPPING = State.new(0.0001, .1, "sip")
var IDLE = State.new(-1, 0, "idle")
var states = [HEAVY, MEDIUM, LIGHT, SIPPING, IDLE]

var drink_spd = 0 setget _drink
var drink_buffer = 0

signal Drinking
signal stop_drinking
signal start_drinking

func _ready():
	set_process(false)
	set_process_input(false)
	
func _process(delta):
	# manage animation state
	# slowly burn down drinking speed
	if self.drink_buffer > 0:
		self.drink_buffer -= SLOW_DOWN_RATE * delta
	else:
		self.drink_spd = max(0, self.drink_spd - (SLOW_DOWN_RATE * delta))
		if self.drink_spd == 0:
			emit_signal("stop_drinking")
	if self.drink_spd > 0:
		emit_signal("Drinking", self.drink_spd * delta)
	
func _input(event):
	# drinking rate increases for every action press
	if event.is_action_released("Drink"):
		self.drink_spd = min(HEAVY.rate, self.drink_spd + DRINK_BOOST)
	
func _drink(value):
	var drank = value > drink_spd
	if drink_spd == 0:
		emit_signal("start_drinking")
	drink_spd = value

	for state in states:
		if value >= state.rate:
			set_animation(state.anim)
			if drank:
				drink_buffer = state.buffer
			break
		
	# show particle system when heavily drinking
	if drink_spd >= HEAVY.rate:
		get_node("spillage").show()
	else:
		get_node("spillage").hide()
		
func _on_game_over():
	get_node("spillage").set_process(false)
	set_process(false)
	set_process_input(false)
	
func _on_restart():
	drink_spd = 0
	drink_buffer = 0
	get_node("spillage").hide()
	get_node("spillage").set_process(true)
	set_process(true)
	set_process_input(true)