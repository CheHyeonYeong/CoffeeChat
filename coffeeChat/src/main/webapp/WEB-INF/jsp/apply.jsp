<!DOCTYPE html>
<html>
<head>
    <title>Coffee Chat Application</title>
</head>
<body>
<h1>Apply for Coffee Chat</h1>
<form action="/apply" method="post">
    <label for="name">Name:</label>
    <input type="text" id="name" name="name" required><br>

    <label for="contact">Contact:</label>
    <input type="text" id="contact" name="contact" required><br>

    <label for="time">Preferred Time:</label>
    <input type="text" id="time" name="preferredTime" required><br>

    <label for="topic">Topic:</label>
    <input type="text" id="topic" name="topic" required><br>

    <button type="submit">Apply</button>
</form>
</body>
</html>
