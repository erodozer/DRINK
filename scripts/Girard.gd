extends AnimatedSprite

var _awareness = 0
var _watching = false
const LOOK_UP_CHANCE = .2
const CYCLE_LENGTH = 1.0
const WATCH_LENGTH = 5
var _cycle = 0.0

signal aware

func _ready():
	set_process(false)
	set_animation("surfing")
	
func _process(delta):
	if !self._watching:
		self._cycle += delta
		if self._cycle > 1: # check every second
			if randf() < LOOK_UP_CHANCE:
				begin_watching()
			self._cycle = 0
	else:
		self._cycle += delta
		if self._awareness >= 1.0:
			become_aware()
		elif self._cycle > WATCH_LENGTH:
			self._cycle = 0
			stop_watching()
			
			
# increase awareness of drinking while watching Nick
func _on_drink(dranked):
	if self._watching:
		_awareness += dranked / 2.8
		get_node("Awareness").set_value(_awareness)
	
func _on_game_over():
	set_process(false)
	
func _on_restart():
	_awareness = 0
	_watching = 0
	_cycle = 0
	set_process(true)
	get_node("Surprise").hide()
	get_node("Awareness").hide()
	get_node("Awareness").set_value(0)
	
func begin_watching():
	set_animation("aware")
	_watching = true
	_awareness = 0
	get_node("Awareness").set_value(0)
	get_node("Awareness").show()

func stop_watching():
	set_animation("surfing")
	_watching = false
	_awareness = 0
	get_node("Awareness").hide()
	get_node("Awareness").set_value(0)

func become_aware():
	emit_signal("aware")
	get_node("Surprise").show()
	get_node("Awareness").hide()
	