import base64
import collections
import PIL.Image as Image
import StringIO


def base64_to_bw_bitmap(b64_image):
    image = Image.open(StringIO.StringIO(base64.decodestring(b64_image)))
    image = image.convert("L")
    content = StringIO.StringIO()
    image.save(content, "BMP")
    content.seek(0)
    bmp = Image.open(content)
    return bmp


def convert_to_marks(bitmap):
    pixels = bitmap.load()
    result = []
    for j in xrange(bitmap.size[1]):
        line = []
        for i in xrange(bitmap.size[0]):
            line.append(int(pixels[i, j] < 70))
        result.append(line)
    return result


def process_answers(b64_jpeg_image):
    bitmap = base64_to_bw_bitmap(b64_jpeg_image)
    marks = convert_to_marks(bitmap)
    answers = find_answer(marks)
    return answers

def find_answer(marks):
    import logging

    X = len(marks)
    Y = len(marks[0])

    logging.warn("X: {}, Y: {}".format(X, Y))

    graph = [[-1 for j in xrange(Y)] for i in xrange(X)]
    group_size = collections.defaultdict(int)

    def flood_fill(x, y, group):
        if graph[x][y] != -1 or marks[x][y] == 0:
            return
        graph[x][y] = group
        group_size[group] += 1
        for dx, dy in [(-1, 0), (0, -1), (1, 0), (0, 1)]:
            if x + dx >= 0 and x + dx < X and y + dy >= 0 and y + dy < Y:
                flood_fill(x + dx, y + dy, group)

    current_group = 0
    for x in xrange(X):
        for y in xrange(Y):
            flood_fill(x, y, current_group)
            current_group += 1

    group_size = {k: v for k, v in group_size.items() if v < 400} # Shadow outlier

    logging.warn("Bigs: {}".format([v for v in group_size.values() if v > 10]))

    biggest_mark = max(group_size.values())

    logging.warn("Biggest size: {}".format(biggest_mark))

    mark_groups = [k for k, v in group_size.items() if v > 10]
    #mark_groups = [k for k, v in group_size.items() if v >= 0.5 * biggest_mark]

    logging.warn("Groups: {}".format(mark_groups))

    group_coordinates = collections.defaultdict(lambda: [0, 0])
    for x in xrange(X):
        for y in xrange(Y):
            if not graph[x][y] in mark_groups:
                continue
            group_coordinates[graph[x][y]][0] += x
            group_coordinates[graph[x][y]][1] += y

    for group in mark_groups:
        group_coordinates[group][0] /= group_size[group]
        group_coordinates[group][1] /= group_size[group]

    logging.warn("Coord: {}".format(group_coordinates))

    coordinates = sorted(group_coordinates.values(), key=lambda(x, y): y)
    y0 = coordinates[0][1]
    dy = min(x[1] - y[1] for x, y in zip(coordinates[1:], coordinates[:-1]))
    tol = 10

    answers_values = dict(a=115, b=90, c=65, d=40, e=15)

    def get_ans_value(x):
        ans = "a"
        v_min = abs(x - answers_values["a"])
        for k, v in answers_values.items():
            if abs(v - x) < v_min:
                v_min = abs(v - x)
                ans = k
        logging.warn("Selected: {}".format(ans))
        return ans

    answers = []
    d = 0
    for index, c in enumerate(coordinates):
        index += d
        while True:
            if abs(c[1] - (y0 + index * dy)) > abs(c[1] - (y0 + (index + 1) * dy)):
                answers.append("z")
                index += 1
                d += 1
                logging.warn("No answer")
            elif c[1] + tol < y0 + index * dy:
                logging.warn("Shit!")
                break
            else:
                logging.warn("Adding {} to answer".format(c))
                answers.append(get_ans_value(c[0]))
                logging.warn("Answers: {}".format(answers))
                break


    height = min(x[1] - y[1] for x, y in zip(coordinates[1:], coordinates[:-1]))

    logging.warn(dict(group_coordinates))
    return answers
