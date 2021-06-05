# This is a sample Python script.

# Press ⌃R to execute it or replace it with your code.
# Press Double ⇧ to search everywhere for classes, files, tool windows, actions, and settings.

# tensorflow와 tf.keras를 임포트합니다
import csv

import tensorflow
from tensorflow import keras

# 헬퍼(helper) 라이브러리를 임포트합니다
import numpy as np
import matplotlib.pyplot as plt


def read_csv(path):
    ret_list = []
    f = open(path, 'r', encoding='utf-8')
    rdr = csv.reader(f)
    for line in rdr:
        ret_list.append(line)
    f.close()
    return ret_list[1:]


def write_csv(path, rwa, predict_list):
    f = open(path, rwa, newline='')
    for p in predict_list:
        f.write(str(p) + '\n')
    f.close()


def print_hi(name):
    # Use a breakpoint in the code line below to debug your script.
    print(f'Hi, {name}')  # Press Ctrl+F8 to toggle the breakpoint.


def tensorflow_check(train_data_x, train_data_y, test_data_x, test_data_y):
    print("tensorflow start")

    print(train_data_x.shape)
    print(train_data_y.shape)
    print(test_data_x.shape)
    print(test_data_y.shape)

    model = keras.Sequential([
        keras.layers.Flatten(input_shape=(1, 887 * 2 + 20 + 7)),
        keras.layers.Dense(2000, activation='relu', kernel_regularizer='L2'),
        keras.layers.Dense(500, activation='relu'),
        keras.layers.Dense(100, activation='relu'),
        keras.layers.Dense(2, activation='softmax')
    ])

    model.compile(optimizer='adam',
                  loss='sparse_categorical_crossentropy',
                  metrics=['accuracy'])
    model.fit(train_data_x, train_data_y, epochs=5, validation_split=0.1, shuffle=True)
    test_loss, test_acc = model.evaluate(test_data_x, test_data_y, verbose=2)

    print('\n테스트 정확도:', test_acc)

    predictions = model.predict(test_data_x)
    # print(predictions)
    # print(type(predictions))
    plist = []
    apply_count = 0
    for p in predictions:
        arg = np.argmax(p)
        if arg == 1:
            apply_count += 1
        plist.append(arg)

    print("prediction: apply - ", apply_count, " / ", len(plist))
    print("test data: apply - ", list_count(test_data_y), " / ", len(test_data_y))
    print("same data: apply - ", list_count(list_and(test_data_y, plist)), " / ", len(test_data_y))
    print(np.array(plist))
    print(test_data_y)

    class_names = ['not apply', 'apply']

    def plot_image(i, predictions_array, true_label, img):
        predictions_array, true_label, img = predictions_array[i], true_label[i], img[i]
        plt.grid(False)
        plt.xticks([])
        plt.yticks([])

        # plt.imshow(img, cmap=plt.cm.binary)

        predicted_label = np.argmax(predictions_array)
        if predicted_label == true_label:
            color = 'blue'
        else:
            color = 'red'

        plt.xlabel("{} {:2.0f}% ({})".format(class_names[predicted_label],
                                             100 * np.max(predictions_array),
                                             class_names[true_label]),
                   color=color)

    def plot_value_array(i, predictions_array, true_label):
        predictions_array, true_label = predictions_array[i], true_label[i]
        plt.grid(False)
        plt.xticks([])
        plt.yticks([])
        thisplot = plt.bar(range(2), predictions_array, color="#777777")
        plt.ylim([0, 1])
        predicted_label = np.argmax(predictions_array)

        thisplot[predicted_label].set_color('red')
        thisplot[true_label].set_color('blue')

    # 올바른 예측은 파랑색으로 잘못된 예측은 빨강색으로 나타냅니다
    num_rows = 5
    num_cols = 3
    num_images = num_rows * num_cols
    plt.figure(figsize=(2 * 2 * num_cols, 2 * num_rows))
    for i in range(num_images):
        plt.subplot(num_rows, 2 * num_cols, 2 * i + 1)
        plot_image(i, predictions, test_data_y, test_data_x)
        plt.subplot(num_rows, 2 * num_cols, 2 * i + 2)
        plot_value_array(i, predictions, test_data_y)
    plt.show()

    model.save('my_model.h5')

    print("tensorflow end")


def list_count(number_list):
    count = 0
    for num in number_list:
        if num != 0: count += 1
    return count


def list_and(number_list_a, number_list_b):
    result_list = []
    for i in range(0, len(number_list_a)):
        if number_list_a[i] == number_list_b[i]:
            result_list.append(number_list_b[i])
        else:
            result_list.append(0)
    return result_list


def rate_vector(rate):
    return_list = [0] * 10
    if rate < 0.1:
        return_list[0] = 1
    elif rate < 0.2:
        return_list[1] = 1
    elif rate < 0.3:
        return_list[2] = 1
    elif rate < 0.4:
        return_list[3] = 1
    elif rate < 0.5:
        return_list[4] = 1
    elif rate < 0.6:
        return_list[5] = 1
    elif rate < 0.7:
        return_list[6] = 1
    elif rate < 0.8:
        return_list[7] = 1
    elif rate < 0.9:
        return_list[8] = 1
    else:
        return_list[9] = 1
    return return_list


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    print_hi('PyCharm')

    train_list = read_csv("train_job/train.csv")  # userID, jobID, applied
    job_tags_list = read_csv("train_job/job_tags.csv")  # jobID, tagID
    user_tags_list = read_csv("train_job/user_tags.csv")  # userID, tagID
    tags_list = read_csv("train_job/tags.csv")  # tagID, keyword
    job_companies_list = read_csv("train_job/job_companies.csv")  # companyID, jobID, companySize

    test_list = read_csv("test_job.csv")  # userID, jobID

    write_csv("output.csv", 'w', ['applied'])
    write_csv("output_all_zero.csv", 'w', ['applied'])

    # userID - tagID(user) - tagID(job) - jobID
    # x: tagVector(user), tagVector(job), companySize / y: applied
    # tag 개수 n 개라고 하면 tagVector [n] 으로 만들 수 있을 것 (값은 0, 1로 표시)

    # 1. userID
    #   user_tags_list : user_tags_list[index] = [userID, tagID] / user_tags_dic[userID] = tag_vector
    # 2. jobID
    #   job_tags_list : job_tags_list[index] = [jobID, tagID] / job_tags_dic[jobID] = tag_vector
    # 3. tagID
    #   tags_list : tags_list[index] = [tagID, keyword] / tags_index_dic[tagID] = index
    # *. type(tagVector)
    #   type(tagVector) = list : [tagIndex1?, tagIndex2?, tagIndex3?, ..., tagIndexN?]
    # 4. companyID
    #   job_companies_list : job_companies_list[index] = [companyID, jobID, companySize] /
    #      job_companysize_dic[jobID] = companySizeVector

    tag_size = len(tags_list)
    # print(tag_size)
    tags_index_dic = {}
    for i in range(0, tag_size):
        tags_index_dic[tags_list[i][0]] = i

    user_tags_dic = {}
    for user_tag in user_tags_list:
        tag_index = tags_index_dic[user_tag[1]]
        if not user_tags_dic.keys().__contains__(user_tag[0]):
            user_tags_dic[user_tag[0]] = [0] * tag_size
        user_tags_dic[user_tag[0]][tag_index] = 1

    job_tags_dic = {}
    for job_tag in job_tags_list:
        tag_index = tags_index_dic[job_tag[1]]
        if not job_tags_dic.keys().__contains__(job_tag[0]):
            job_tags_dic[job_tag[0]] = [0] * tag_size
        job_tags_dic[job_tag[0]][tag_index] = 1

    company_size_index = {'1-10': 0, '11-50': 1, '51-100': 2, '101-200': 3, '201-500': 4, '501-1000': 5,
                          '1000 이상': 6}
    job_companysize_dic = {}
    for job_companies_size in job_companies_list:
        jobID = job_companies_size[1]
        job_companysize_dic[jobID] = [0] * len(company_size_index)
        if company_size_index.__contains__(job_companies_size[2]):
            job_companysize_dic[jobID][company_size_index[job_companies_size[2]]] = 1
    print(job_companysize_dic)

    data_x = []
    data_y = []
    for data in train_list:
        userID = data[0]
        jobID = data[1]
        applied = int(data[2])
        count_user_tag = list_count(user_tags_dic[userID])
        count_job_tag = list_count(job_tags_dic[jobID])
        count_cross_tag = list_count(list_and(user_tags_dic[userID], job_tags_dic[jobID]))
        rate_list = [count_cross_tag / count_user_tag, count_cross_tag / count_job_tag]
        rate_vector_list = rate_vector(count_cross_tag / count_user_tag) + rate_vector(count_cross_tag / count_job_tag)
        data_x.append(user_tags_dic[userID] + job_tags_dic[jobID] + rate_vector_list + job_companysize_dic[jobID])
        # data_x.append(list_and(user_tags_dic[userID], job_tags_dic[jobID]) + rate_list + job_companysize_dic[jobID])
        # data_x.append(list_plus(user_tags_dic[userID], job_tags_dic[jobID]))
        data_y.append(applied)
    # print(data_x)

    print(type(data_x))
    print(type(data_y))
    np_data_x = np.array(data_x)
    np_data_y = np.array(data_y)
    print(type(np_data_x))
    print(type(np_data_y))
    print(np_data_x.shape)
    print(np_data_y.shape)

    data_size = len(np_data_x)
    # test_size = int(data_size * 0.1)
    test_size = 500

    test_data_x = np_data_x[data_size - test_size:]
    test_data_y = np_data_y[data_size - test_size:]
    # np_data_x = np_data_x[:data_size-test_size]
    # np_data_y = np_data_y[:data_size-test_size]
    print(len(test_data_x))
    print(len(test_data_y))
    print(len(np_data_x))
    print(len(np_data_y))

    tensorflow_check(np_data_x, np_data_y, test_data_x, test_data_y)

    test_model = tensorflow.keras.models.load_model('my_model.h5')
    test_model.summary()
    test_x = []
    print(len(test_list))
    for data in test_list:
        userID = data[0]
        jobID = data[1]
        count_user_tag = list_count(user_tags_dic[userID])
        count_job_tag = list_count(job_tags_dic[jobID])
        count_cross_tag = list_count(list_and(user_tags_dic[userID], job_tags_dic[jobID]))
        rate_list = [count_cross_tag / count_user_tag, count_cross_tag / count_job_tag]
        rate_vector_list = rate_vector(count_cross_tag / count_user_tag) + rate_vector(count_cross_tag / count_job_tag)
        test_x.append(user_tags_dic[userID] + job_tags_dic[jobID] + rate_vector_list + job_companysize_dic[jobID])

    np_test_x = np.array(test_x)

    print("test prediction start")
    test_prediction = test_model.predict(np_test_x)
    print(test_prediction)

    test_prediction_list = []
    all_zero = []
    apply_count = 0
    for p in test_prediction:
        arg = np.argmax(p)
        test_prediction_list.append(arg)
        all_zero.append(0)

    print("write start")
    write_csv("output.csv", 'a', test_prediction_list)
    write_csv("output_all_zero.csv", 'a', all_zero)
    print("write finish")

    # userID - tagVector
    # jobID - tagVector
    # [userID][tagID] = 1
    # for user_tag in user_tags_list:
    # user_vectors_list[user_tag[0]][tags_index_dic[user_tag[1]]] = 1
    # print(user_vectors_list[user_tag])
    # print(user_vectors_list)
    # for job_tag in job_tags_list:

    # x: tagVectorUser, tagVectorJob / y: applied
    # companySize 는 따로..

    # tensorflow_practice()
