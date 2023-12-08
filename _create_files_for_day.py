#!/usr/bin/env python3
import os
import sys
import urllib.request
from html.parser import HTMLParser
from pathlib import Path

YEAR = 2023
SRC_DIR = os.path.join("src", "main", "groovy")
INPUT_DIR = os.path.join("src", "main", "resources", "input")
INPUT_FILE_TEMPLATE = "aoc{}_{:02}_input.txt"
SCRIPT_FILE_TEMPLATE = "aoc{}_{:02}.groovy"
URL_TEMPLATE = "https://adventofcode.com/{}/day/{}"

SCRIPT_TEMPLATE = """\
#!/usr/bin/env groovy
/**
 * {heading}
 * {url}
 */
package day{day:02}

import static utils.Utils.getFile

class Day{day:02} {{
    final static Integer day = {day}

    static void main(String[] args) {{
        
    }}
}}

"""


class HeadingParser(HTMLParser):
    def __init__(self) -> None:
        super().__init__()
        self.in_main = False
        self.in_article = False
        self.in_h2 = False
        self.heading: str | None = None

    def handle_starttag(self, tag: str, attrs: list[tuple[str, str | None]]) -> None:
        if tag == "main":
            self.in_main = True
        elif tag == "article":
            self.in_article = True
        elif tag == "h2":
            self.in_h2 = True

    def handle_endtag(self, tag: str) -> None:
        if tag == "main":
            self.in_main = False
        elif tag == "article":
            self.in_article = False
        elif tag == "h2":
            self.in_h2 = False

    def handle_data(self, data: str) -> None:
        if self.in_main and self.in_article and self.in_h2 and self.heading is None:
            self.heading = data.strip()


def get_html_content(url: str) -> str:
    response = urllib.request.urlopen(url)
    data = response.read()
    return data.decode("utf-8")


def get_heading(url: str) -> str | None:
    parser = HeadingParser()
    parser.feed(get_html_content(url))
    return parser.heading


# def get_heading(url):
#    return "HEADING"


def main() -> None:
    day_number = int(sys.argv[1])
    url = URL_TEMPLATE.format(YEAR, day_number)
    heading = get_heading(url)
    src_dir = Path(SRC_DIR) / f"day{day_number:02}"
    input_dir = Path(INPUT_DIR)
    input_file_name = INPUT_FILE_TEMPLATE.format(YEAR, day_number)
    script_file_name = SCRIPT_FILE_TEMPLATE.format(YEAR, day_number)

    os.makedirs(input_dir, exist_ok=True)
    with open(input_dir / input_file_name, "w"):
        pass

    os.makedirs(src_dir, exist_ok=True)
    with open(src_dir / script_file_name, "w", encoding="utf-8") as scriptfile:
        scriptfile.write(
            SCRIPT_TEMPLATE.format(
                heading=heading,
                url=url,
                day=day_number)
        )

    os.chmod(src_dir / script_file_name, 0o755)


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print(f"Usage: {sys.argv[0]} day_number")
        sys.exit(1)

    main()
