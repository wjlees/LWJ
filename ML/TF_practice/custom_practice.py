# tensorflow와 tf.keras를 임포트합니다
import copy

import numpy
import tensorflow
from tensorflow import keras

# 헬퍼(helper) 라이브러리를 임포트합니다
import numpy as np
import matplotlib.pyplot as plt

example = [[1, 2, 3, 4, 5, 6],  # 1
           [7, 8, 9, 10, 11, 12],  # 2
           [13, 14, 15, 16, 17, 18],  # 3
           [19, 20, 21, 22, 23, 24],  # 4
           [25, 26, 27, 28, 29, 30],  # 5
           [31, 32, 33, 34, 35, 36],  # 6
           [37, 38, 39, 40, 41, 42],  # 7
           [43, 44, 45, 1, 2, 3]]  # 8
example_y = []
count = []
count_list = []

numbers = []
DIV = 2
x = []

def plot_value_array(i, predictions_array, true_label):
    predictions_array, true_label = predictions_array[i], true_label[i]
    plt.grid(False)
    plt.xticks([])
    plt.yticks([])
    thisplot = plt.bar(range(45), predictions_array, color="#777777")
    plt.ylim([0, 0.3])
    predicted_label = np.argmax(predictions_array)

    thisplot[predicted_label].set_color('red')
    thisplot[true_label].set_color('blue')


def div_list(list):
    for i in range(0, len(list)):
        list[i] /= DIV


def custom_ML():
    print("CUSTOM ML")
    for i in range(0, 45):
        count.append(0)

    for i in range(0, len(example)):
        example_y.append(([0 for i in range(0, 45)]))
        div_list(count)
        for j in range(0, 6):
            count[example[i][j] - 1] += 1
            example_y[i][example[i][j] - 1] += 1
        # print(count)
        count_list.append(list(count))
        # .copy() 하지 않으면 shallow copy 되어 count와 count_list[0~i-1] 들이 같이 변함
        # .copy() 를 통해서 deep copy? 해야한다.
    # print(count_list)

    # count_list[0] 으로 example[1]을 예상하므로
    # x: count_list[0], y: example[1]
    # data - x: count_list[:-1], y:exmaple[1:]
    train_x = count_list[:-1]
    len_x = len(train_x)
    train_x_new = []
    train_y_new = []
    for i in range(0, len_x):
        for j in range(0, 6):
            train_x_new.append(list(train_x[i]))
            train_y_new.append(example[i][j]-1)

    # train_x = train_x * 6
    # train_y = [ex[0]-1 for ex in example[1:]]\
    #           + [ex[1]-1 for ex in example[1:]]\
    #           + [ex[2]-1 for ex in example[1:]]\
    #           + [ex[3]-1 for ex in example[1:]]\
    #           + [ex[4]-1 for ex in example[1:]]\
    #           + [ex[5]-1 for ex in example[1:]]

    test_x = train_x_new[-6:]
    test_y = train_y_new[-6:]
    print(test_x)
    print(test_y)
    train_x_new = train_x_new[:-6]
    train_y_new = train_y_new[:-6]
    train_x_new = numpy.array(train_x_new)
    train_y_new = numpy.array(train_y_new)
    test_x = numpy.array(test_x)
    test_y = numpy.array(test_y)

    print("=================================")

    print("==x==")
    print(train_x_new)
    print("==y==")
    print(train_y_new)
    print(train_x_new.shape)
    print(train_y_new.shape)

    model = keras.Sequential([
        keras.layers.Dense(128, activation='relu'),
        keras.layers.Dense(45, activation='softmax')
    ])

    model.compile(optimizer='adam',
                  loss='sparse_categorical_crossentropy',
                  metrics=['accuracy'])

    print("compile OK")
    model.fit(train_x_new, train_y_new, epochs=100)
    print(test_x)
    print(test_y)
    test_loss, test_acc = model.evaluate(test_x, test_y, verbose=2)

    print('\n테스트 정확도:', test_acc)

    predictions = model.predict(test_x)
    print(predictions[0])

    plt.figure(figsize=(12, 7))
    plot_value_array(0, predictions, test_y)
    plt.xticks(range(0, 45))
    plt.show()


def custom_main():
    print("== custom practice start ==")
    custom_ML()
    print("== custom practice end ==")
