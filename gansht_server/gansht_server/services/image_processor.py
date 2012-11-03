import base64
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
            line.append(int(pixels[i, j] < 128))
        result.append(line)
    return result


def process_answers(b64_jpeg_image):
    bitmap = base64_to_bw_bitmap(b64_jpeg_image)
    marks = convert_to_marks(bitmap)

    with open("marks.txt", "w") as f:
        for line in marks:
            print >> f, "".join(map(str, line))

    test_name = find_test(marks)
    answers = find_answer(marks)

    return test_name, answers


def find_test(marks):
    return


def find_answer(marks):
    return
