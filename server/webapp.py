import tornado.web
import logging
import db
import json
import datetime
import tornado.ioloop


original_sigint = None
g_stop_wait = False


class MRBSWebApplication(tornado.web.Application):
    def __init__(self):
        self.__handlers = [
            (r"/get", GetBookingsHandler),
            (r"/add", AddBookingHandler),
            (r"/update", UpdateBookingHandler),
            (r"/delete", DeleteBookingHandler),
        ]

        super().__init__(self.__handlers)

        self.db = db.MRBSDb()

    def run(self, port=8081):
        self.listen(port)
        tornado.ioloop.IOLoop.instance().start()

    def stop(self):
        tornado.ioloop.IOLoop.instance().stop()


class MRBSTornadoHandler(tornado.web.RequestHandler):
    STATUS_SUCCESS = 200
    STATUS_PENDING = 202
    STATUS_BAD_REQUEST = 400
    STATUS_TIMEOUT = 408
    STATUS_INTERNAL_ERROR = 500
    STATUS_TRY_AGAIN = 503

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self._logger = logging.getLogger()

    def data_received(self, chunk):
        self._logger.warning("What is this [{}]????".format(chunk))

    def _complete_finish(self, status, message_to_write):
        self._logger.debug("Finish request with status: {} and message: {}".format(status, message_to_write))
        self.set_status(status)
        self.write(message_to_write)
        self.finish()

    def _finish_with_bad_request(self, message):
        self._complete_finish(self.STATUS_BAD_REQUEST, {"status": "error", "reason": "Bad request. " + message})


class GetBookingsHandler(MRBSTornadoHandler):
    def get(self):
        last_booking_id = self.get_argument("lastBookingId", True)

        bookings = self.application.db.get_latest_bookings(last_booking_id)
        bookings_list = []

        for booking_tuple in bookings:
            bookings_list.append({
                "Id": booking_tuple[0],
                "Title": booking_tuple[1],
                "Year": booking_tuple[2],
                "Month": booking_tuple[3],
                "Day": booking_tuple[4],
                "StartHour": booking_tuple[5],
                "StartMin": booking_tuple[6],
                "EndHour": booking_tuple[7],
                "EndMin": booking_tuple[8],
                "Room": booking_tuple[9]
            })

        self.write(json.dumps(bookings_list))


class AddBookingHandler(MRBSTornadoHandler):
    def post(self):
        booking = json.loads(self.get_argument("booking"))
        title = booking["Title"]
        day = booking["Day"]
        month = booking["Month"]
        year = booking["Year"]
        start_hour = booking["StartHour"]
        start_min = booking["StartMin"]
        end_hour = booking["EndHour"]
        end_min = booking["EndMin"]
        room = booking["Room"]

        print("POST request on '/add' with Title: {}, Day: {}, Month: {}, Year: {}, StartHour: {}, StartMin: {}, "
              "EndHour: {}, EndMin: {}, Room: {}".format(title, day, month, year, start_hour, start_min, end_hour,
                                                         end_min, room))

        bookings = self.application.db.get_bookings_for_day_and_room(year, month, day, room)
        bookings_list = []

        for booking_tuple in bookings:
            bookings_list.append({
                "Id": booking_tuple[0],
                "Title": booking_tuple[1],
                "Year": booking_tuple[2],
                "Month": booking_tuple[3],
                "Day": booking_tuple[4],
                "StartHour": booking_tuple[5],
                "StartMin": booking_tuple[6],
                "EndHour": booking_tuple[7],
                "EndMin": booking_tuple[8],
                "Room": booking_tuple[9]
            })

        last_row_id = None

        start_time = datetime.datetime.strptime("%d:%d" % (start_hour, start_min), "%H:%M")
        end_time = datetime.datetime.strptime("%d:%d" % (end_hour, end_min), "%H:%M")
        for booking in bookings_list:
            start_time1 = datetime.datetime.strptime("%d:%d" % (booking["StartHour"], booking["StartMin"]), "%H:%M")
            end_time1 = datetime.datetime.strptime("%d:%d" % (booking["EndHour"], booking["EndMin"]), "%H:%M")
            if self.overlap(start_time, end_time, start_time1, end_time1):
                last_row_id = -1
                break

        if last_row_id is None:
            last_row_id = self.application.db.insert_booking(title, year, month, day, start_hour, start_min, end_hour,
                                                             end_min, room)

        self.write({"Id": last_row_id})

    @staticmethod
    def overlap(start_time, end_time, start_time1, end_time1):
        for time in (start_time, end_time):
            if start_time1 < time < end_time1:
                return True
        for time in (start_time1, end_time1):
            if start_time < time < end_time:
                return True
        return False


class UpdateBookingHandler(MRBSTornadoHandler):
    def post(self):
        booking = json.loads(self.get_argument("booking"))
        id = booking["Id"]
        title = booking["Title"]
        day = booking["Day"]
        month = booking["Month"]
        year = booking["Year"]
        start_hour = booking["StartHour"]
        start_min = booking["StartMin"]
        end_hour = booking["EndHour"]
        end_min = booking["EndMin"]
        room = booking["Room"]

        print("POST request on '/update' with Title: {}, Day: {}, Month: {}, Year: {}, StartHour: {}, StartMin: {}, "
              "EndHour: {}, EndMin: {}, Room: {}".format(title, day, month, year, start_hour, start_min, end_hour,
                                                         end_min, room))

        row_count = self.application.db.update_booking(id, title, year, month, day, start_hour, start_min, end_hour,
                                                       end_min, room)

        self.write({"RowCount": row_count})


class DeleteBookingHandler(MRBSTornadoHandler):
    def post(self):
        booking_id = self.get_argument("bookingId", True)

        print("POST request on '/delete' with Id: {}".format(booking_id))
        row_count = self.application.db.delete_booking(booking_id)

        self.write({"RowCount": row_count})


if __name__ == '__main__':
    MRBSWebApplication().run()
