extends Node

var _time = 0 setget _set_time
var _score = 0
var _has_started = false
var _is_drinking = false
var is_gameover = false

signal GameOver
signal Restart
signal StartGame

func _ready():
	set_process(false)
	set_process_input(true)

# reset all game variables
func _reset():
	self._time = 0
	self._score = 0
	is_gameover = false
	
func _gameover():
	set_process(false)
	is_gameover = true
	emit_signal("GameOver")
	
func _process(delta):
	if _is_drinking:
		self._time = self._time + delta
		
func _input(event):
	if event.is_action("Drink") and !_has_started:
		_has_started = true
		set_process(true)
		emit_signal("StartGame")
	if event.is_action("Restart") and is_gameover:
		emit_signal("Restart")
	
func _on_drink(amount):
	_score += amount
	get_node("UI/Score/OJ").set_text("%.02f" % _score)
	
func _set_time(seconds):
	# updates the time label
	var ms = int(seconds * 1000) % 1000
	var s = int(seconds) % 60
	var mi = int(seconds) / 60
	_time = seconds
	get_node("UI/Timer/Time").set_text("%02d:%02d:%03d"  % [mi, s, ms])

func _on_start_drinking():
	_is_drinking = true

func _on_stop_drinking():
	_is_drinking = false