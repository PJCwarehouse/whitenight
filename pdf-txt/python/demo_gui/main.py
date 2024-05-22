# -*- coding: utf-8 -*-
import os
import sys
import random
import argparse

import cv2
import numpy as np
from pdf2image import convert_from_path


# self-defined modules to be added to PYTHONPATH
project_root = os.path.dirname(os.path.abspath(__file__)) + '/..'
sys.path.append(project_root)
from pdfocr import PdfOcrTool
# end of self-defined module list


def get_args():
    parser = argparse.ArgumentParser()
    parser.add_argument("--pdfFileName", type=str, required=True)
    parser.add_argument("--start", type=int, required=True)
    parser.add_argument("--end", type=int, required=True)
    args = parser.parse_args()

    return args.pdfFileName, args.start, args.end


def main():
    pdf_file_name, start_page, end_page = get_args()

    # init pdf ocr tool
    ocr = PdfOcrTool(newline="\n")

    # read in pdf pages and transform them to images
    print("Loading page(s) from PDF file...")

    current_dir = os.path.dirname(os.path.abspath(__file__))

    # 退两级到项目根目录，然后进入 pdf 目录
    pdf_path = os.path.join(current_dir, os.pardir, os.pardir, 'pdf', pdf_file_name)
    pdf_path = os.path.abspath(pdf_path)  # 将相对路径转换为绝对路径

    images = convert_from_path(pdf_path,
                               first_page=start_page,
                               last_page=end_page)

    # Open a text file to write the output
    output_file = os.path.join(current_dir, os.pardir, os.pardir, 'txt', pdf_file_name.replace(".pdf", ".txt"))
    with open(output_file, "w") as f:
        for img_idx, img in enumerate(images):
            page_num = start_page + img_idx
            print("Processing page %s ..." % page_num)

            # 将 PIL 图像转换为 OpenCV 图像
            page_img = cv2.cvtColor(np.array(img), cv2.COLOR_RGB2BGR)
            labeled_textbox = ocr.predict(page_img)

            # 写入 OCR 结果到文本文件
            for label, textbox in labeled_textbox.items():
                f.write(f"<{label}>\n{textbox[1]}\n")
                # f.write("<{}>\n{}\n".format(label, textbox[1]))
                label_color = random.choices(range(256), k=3)
                x, y, w, h = textbox[0]
                cv2.rectangle(page_img, (x, y), (x + w, y + h), label_color, 3)
                cv2.putText(page_img, label, (x, y), cv2.FONT_HERSHEY_SIMPLEX, 1,
                            label_color, 4, cv2.LINE_AA)

    print(f"TXT文件存放位置为 {output_file}")



if __name__ == "__main__":
    main()
