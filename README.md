# 目录
* [0.目录](#目录)
* [1.PresentationLayer](#'PresentationLayer')
	* [1.1CaptureActivity]
	* [1.2CaptureFragment]
	* [1.3CapturePresenter]
	* [1.4CameraState]
* [2.DomainLayer](#DomainLayer)
	* [2.1UseCase]
* [3.DataLayer](#DataLaye)

# PresentationLayer
* CaptureActivity
* CaptureFragment
* CapturePresenter
* CameraState
	* state
		- UNKNOWN
		- IDLE
		- INWORK
		- ERROR
	* funcation
		- void reInit()
		- void onIdle()
		- void inWork()
		

# DomainLayer
* UseCase
	* InitEngine
	* SwitchCamera
	* StartRecording
	* StopRecording
	* CancelRecording
	* ChooseFilter


# DataLaye
