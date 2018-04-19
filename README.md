# 目录
* [0.目录](#目录)
* [1.PresentationLayer](#presentation-layer)
	* 1.1CaptureActivity
	* 1.2CaptureFragment
	* 1.3CapturePresenter
	* 1.4CameraState
* [2.DomainLayer](#domain-layer)
	* 2.1UseCase
* [3.DataLayer](#data-layer)

<b id="presentation-layer"></b>
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
		
<b id="domain-layer"></b>
# DomainLayer
* UseCase
	* InitEngine
	* SwitchCamera
	* StartRecording
	* StopRecording
	* CancelRecording
	* ChooseFilter

<b id="data-layer"></b>
# DataLaye
