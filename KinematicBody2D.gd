extends KinematicBody2D

# class member variables go here, for example:
# var a = 2
# var b = "textvar"
const SPEED = 125
const SENSITIVITY = 50
var health = 100
var motion = Vector2()
var is_slowed = false
var mouse_pos
var cur_tile
onready var sprite = get_node("sprite")
onready var map = get_parent().get_node("TileMap/CollisionLayer")
var anim = "down"

func is_deadzone():
	return abs(mouse_pos.x - position.x) < SENSITIVITY || !abs(mouse_pos.y - position.y) < SENSITIVITY
	pass

func die():
	print("The player is dead")
	#TODO
	pass

func _ready():
	# Called when the node is added to the scene for the first time.
	# Initialization here
	pass
	
func _physics_process(delta):
	if health <= 0:
		die()
	if Input.is_action_pressed("ui_down"):
		motion.y += 1
	elif Input.is_action_pressed("ui_up"):
		motion.y -= 1
	else:
		motion.y = 0
	if Input.is_action_pressed("ui_right"):
		motion.x += 1
	elif Input.is_action_pressed("ui_left"):
		motion.x -= 1
	else:
		motion.x = 0
	if motion == Vector2.ZERO:
		sprite.playing = false
		sprite.set_frame(0)
	mouse_pos = get_global_mouse_position()
	if mouse_pos.y > position.y:
		anim = "down"
	elif mouse_pos.y < position.y:
		anim = "up"
	if mouse_pos.x > position.x and !is_deadzone():
		anim= "right"
	elif mouse_pos.x < position.x and !is_deadzone():
		anim =  "left"
	
	cur_tile = map.world_to_map(global_position)
	var tile_name = map.tile_set.tile_get_name(map.get_cellv(cur_tile))
	if tile_name == "Toxic":
		health = health - 0.1
	elif tile_name == "Tar":
		is_slowed = true
	else:
		is_slowed = false
		health = health + 0.05
	
	sprite.play(anim)
	motion = motion.normalized()
	move_and_slide(motion*SPEED*0.5) if is_slowed else move_and_slide(motion*SPEED)	
	pass