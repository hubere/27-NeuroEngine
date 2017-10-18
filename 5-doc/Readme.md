
Getting up and running

1) Installed python-3.5.4-amd64.exe as described at https://www.tensorflow.org/install/install_windows
2) Installed vc_redist.x64.exe from https://www.microsoft.com/en-us/download/details.aspx?id=53587
3) Installed Eclipse Oxygen with PyDev in D:\usr\huber\Projekte\2-development\java-oxygen





Infos

	logfile für tensorflow:  D:\tmp\tensorflow


TensorBoard:
	NOTE! start it in D: ! like this:
	
	D:\>tensorboard --logdir=TRAIN:\tmp\tensorflow\mnist\logs\mnist_with_summaries\train --debug
	
	You cannot start it in C: like this:
	
	C:\>tensorboard --logdir=D:\tmp\tensorflow\mnist\logs\mnist_with_summaries\train --debug
	
	because it will interpret the 'D:' as a name.
	
	
	Browse http://localhost:6006/ 
	

Useful links:

	https://indico.io/blog/tensorflow-data-inputs-part1-placeholders-protobufs-queues/
	https://indico.io/blog/python-deep-learning-frameworks-reviewed/
	https://keras.io/#keras-deep-learning-library-for-theano-and-tensorflow
	http://camron.xyz/index.php/2016/09/13/hybrid_learning/
	
					

Known Issues

	ImportError: No module named '_pywrap_tensorflow_internal'
	=> MSVCP140.DLL was missing, see https://stackoverflow.com/questions/43751418/tensorflow-on-windows-importerror-no-module-named-pywrap-tensorflow-internal
	
	
