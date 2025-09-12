# Smart Campus Guide
University Indoor Scene Classification research project implemented as a mobile app and published as a research paper. This work was published in [IEEE ICIRCA 2022](https://ieeexplore.ieee.org/document/9985516).

**Abstract**

Smart Campus Guide is a research project and mobile app that uses deep learning to identify indoor lab environments on campus and provide related information. Dataset was 4,502 images covering four classes (Apple Lab, Sophos Lab, Sophos Rack, Virtual Reality Lab). Using transfer learning, I fine-tuned several pre-trained CNN models (VGG16, ResNet50, InceptionV3, Xception, etc.) on this dataset. I evaluated each model on metrics including accuracy, recall, precision, F1 score, inference time, model size, and FLOPs. Among the models, Xception achieved the highest performance (99.75% test accuracy, 99.60% F1 score). The trained model is embedded in an Android app that classifies a live or selected image and then displays additional facility details based on the predicted class.

**Methodology**

Other team memeber collected and labeled images of our university labs. The dataset contains 4,502 color images (256×256) across four categories. I split the data into 91% training and 9% testing. To improve generalization, I applied extensive data augmentation (random shifts, flips, zooms, rotations, shear) during training.

We employed a transfer learning approach: each image is fed through a pre-trained CNN backbone to extract features, followed by a new classifier head. Our primary experiments (as reported in the IEEE paper) used four popular CNN architectures – VGG16, ResNet50, InceptionV3, and Xception – each with its final layer replaced by a 4-way softmax. The last layer outputs correspond to the four lab classes. We also conducted extended experiments with additional models: MobileNet, MobileNetV2, NASNet-Mobile, and EfficientNet B0–B4, to explore trade-offs between accuracy, model size, and speed. Each model was fine-tuned for 5, 10 and 15 epochs (batch size 32) with appropriate optimizers and learning rates, and all were evaluated on the same test split.
<img width="1082" height="648" alt="image" src="https://github.com/user-attachments/assets/21d4dd8d-29dd-47d0-bff4-6b84b7a620bf" />


**Mobile App Implementation**

The resulting best model was exported (e.g., to TensorFlow Lite) and integrated into an Android app. The app’s interface allows the user to capture or select a photo of a lab scene. The image is then passed through the on-device CNN to predict one of the four lab classes. Once classified, the app displays the predicted lab name and fetches additional information (lab resources, equipment, location, etc.) from a backend database. This lets visitors or students simply take a photo of a lab, get an instant identification, and learn details about the facility. The Android project (Java/Kotlin) and web service are included in the repository.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<img width="326" height="622" alt="image" src="https://github.com/user-attachments/assets/271cb8fc-f2cb-4aef-936a-68fe51d00bf3" /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<img width="336" height="608" alt="image" src="https://github.com/user-attachments/assets/8745f7dd-1589-4903-a9d3-ef021a84663f" /></br></br></br></br></br>
<img width="1258" height="463" alt="image" src="https://github.com/user-attachments/assets/6574a20a-d7ae-442d-acde-d9dc1958e62d" />




**Results**

We evaluated model performance on the test set using accuracy, recall, precision, F1 score, model size, inference time, and B-FLOPs. The table below summarizes highlights (from 10-epoch runs). Our best models achieved near-perfect accuracy: InceptionV3 reached 100% test accuracy, and Xception reached 99.75%. Several EfficientNet variants were also highly accurate: EfficientNet B3/B4 both trained to 100% and gave ~99.5–100% test accuracy. In contrast, lightweight models were smaller but slightly less accurate: for example, MobileNet (14.9 MB) attained ~99.00% test accuracy, and MobileNetV2 (11.9 MB) ~97.77%. These results confirm that transfer learning can yield very accurate lab classifiers, with choice of model providing trade-offs in size and speed.

<img width="1047" height="592" alt="image" src="https://github.com/user-attachments/assets/3092cdaa-5ea1-4e4e-9541-31a8afc82fd6" />

**Note:** These results reflect performance on a small, domain-specific dataset (4 classes, 4,502 images).  
Such high accuracy is expected in this controlled setting and may not generalize to larger or more diverse datasets.

