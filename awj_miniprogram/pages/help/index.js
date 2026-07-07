Page({
  showHelp(e) {
    const title = e.currentTarget.dataset.title;
    const content = e.currentTarget.dataset.content;
    wx.showModal({
      title: title,
      content: content,
      showCancel: false
    });
  }
});
