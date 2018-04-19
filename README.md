# 目录
* [0.目录](#目录)
* [1.PresentationLayer](#PresentationLayer) 
	* [1.1CaptureActivity](#CaptureActivity)
	* [1.2CaptureFragment](#CaptureFragment)
	* [1.3CapturePresenter](#CapturePresenter)
	* [1.4CameraState](#CameraState)
* [2.DomainLayer](#DomainLayer)
	* [2.1UseCase](#UseCase)
* [3.DataLayer](#DataLayer)


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

# DataLayer
