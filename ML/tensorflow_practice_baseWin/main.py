# This is a sample Python script.

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.

# tensorflow와 tf.keras를 임포트합니다
import tensorflow as tf
from tensorflow import keras

# 헬퍼(helper) 라이브러리를 임포트합니다
import numpy as np
import matplotlib.pyplot as plt


def print_hi(name):
    # Use a breakpoint in the code line below to debug your script.
    print(f'Hi, {name}')  # Press Ctrl+F8 to toggle the breakpoint.

def tensorflow_practice():
    fashion_mnist = keras.datasets.fashion_mnist
    # type(fashion_mnist) : <class 'module'>
    (train_images, train_labels), (test_images, test_labels) = fashion_mnist.load_data()
    # type(train_images) : <class 'numpy.ndarray'>
    # data 70000개 중에서 훈련에 60000개, 테스트에 10000개 사용함
    # load_data 하면 알아서 이렇게 넣어줌.

    # 0    T - shirt / top
    # 1    Trouser
    # 2    Pullover
    # 3    Dress
    # 4    Coat
    # 5    Sandal
    # 6    Shirt
    # 7    Sneaker
    # 8    Bag
    # 9    Ankle boot
    class_names = ['T-shirt/top', 'Trouser', 'Pullover', 'Dress', 'Coat',
                   'Sandal', 'Shirt', 'Sneaker', 'Bag', 'Ankle boot']
    print(train_images.shape)
    print(train_labels.shape)
    print(test_images.shape)
    print(test_labels.shape)
    # (60000, 28, 28)
    # (60000,)
    # (10000, 28, 28)
    # (10000,)
    # 크기는 28*28, 값은 0~255 (색)

    # plt.figure()
    # plt.imshow(train_images[0])
    # plt.colorbar()
    # plt.grid(False)
    # plt.show()

    train_images = train_images / 255.0
    test_images = test_images / 255.0

    plt.figure(figsize=(10, 10))
    for i in range(25):
        plt.subplot(5, 5, i + 1)
        plt.xticks([])
        plt.yticks([])
        plt.grid(False)
        plt.imshow(train_images[i], cmap=plt.cm.binary)
        plt.xlabel(class_names[train_labels[i]])
    plt.show()

    model = keras.Sequential([
        keras.layers.Flatten(input_shape=(28, 28)),
        keras.layers.Dense(128, activation='relu'),
        keras.layers.Dense(10, activation='softmax')
    ])

    model.compile(optimizer='adam',
                  loss='sparse_categorical_crossentropy',
                  metrics=['accuracy'])
    model.fit(train_images, train_labels, epochs=40)
    test_loss, test_acc = model.evaluate(test_images, test_labels, verbose=2)

    print('\n테스트 정확도:', test_acc)


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    print_hi('PyCharm')
    tensorflow_practice()
    print("practice end " + tf.__version__)

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
